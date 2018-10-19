package com.example.demo.kafka.integrate.collection.dao.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.kafka.integrate.collection.annotation.ORMHBaseColumn;
import com.example.demo.kafka.integrate.collection.annotation.ORMHBaseTable;
import com.example.demo.kafka.integrate.collection.dao.BaseDao;
import com.example.demo.kafka.integrate.collection.dao.share.ThreadShare;
import com.example.demo.kafka.integrate.collection.dto.PageDto;
import com.example.demo.kafka.integrate.collection.entity.BaseEntity;
import com.example.demo.kafka.integrate.collection.vo.PageVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unchecked")
public class BaseDaoImpl implements BaseDao {
	
	Logger logger=LoggerFactory.getLogger(BaseDaoImpl.class);
	
    /**
     * 对象使用了ORMHBaseTable 和 ORMHBaseColumn 注解
     *
     * @param rowKey
     * @param obj
     */
    public void save(String rowKey,Object obj){
        Field[] fields =  obj.getClass().getDeclaredFields();//获取属性
        ORMHBaseTable ormhBaseTable = obj.getClass().getAnnotation(ORMHBaseTable.class);
        if(ormhBaseTable==null){
            throw new RuntimeException("该实体没有使用ORMHBaseTable注解");
        }
        String tableName=ormhBaseTable.tableName();
        if(StringUtils.isBlank(tableName)){
            throw new RuntimeException("请声明表名！");
        }
        Put put=new Put(Bytes.toBytes(rowKey));
        for(Field f:fields){
            String filedName = f.getName();
            String value=null;
            try {
                value = BeanUtils.getProperty(obj, filedName);//值
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            logger.info("属性名称:【"+filedName+"】  值：【"+value+"】");
            if(StringUtils.isBlank(value)){
                continue;
            }
            Annotation annotation = f.getAnnotation(ORMHBaseColumn.class);//获取指定注解
            if(annotation!=null){//有该类型的注解存在
                ORMHBaseColumn ormhBaseColumn= (ORMHBaseColumn) annotation;
                String family = ormhBaseColumn.family();//列族
                String qualifier = ormhBaseColumn.qualifier();//列
                put.addColumn(Bytes.toBytes(family),Bytes.toBytes(qualifier),Bytes.toBytes(value));
            }
        }
        Connection connection = ThreadShare.getConnection();
        Table tbl = null;
        try {
            TableName table = TableName.valueOf(tableName);
            tbl = connection.getTable(table);
            tbl.put(put);
            tbl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 对象使用了ORMHBaseTable 和 ORMHBaseColumn 注解
     * @param rowKey
     * @param T
     * @param <T>
     * @return
     */
    public <T> T get(String rowKey, Class<? extends BaseEntity> T) {
        Connection connection = ThreadShare.getConnection();
        BaseEntity entity = null;
        ORMHBaseTable ormhBaseTable = T.getAnnotation(ORMHBaseTable.class);
        if(ormhBaseTable==null){
            throw new RuntimeException("该实体没有使用ORMHBaseTable注解");
        }
        String tableName=ormhBaseTable.tableName();
        if(StringUtils.isBlank(tableName)){
            throw new RuntimeException("请声明表名！");
        }
        try {
            Get get = new Get(Bytes.toBytes(rowKey));
            Table hTable = connection.getTable(TableName.valueOf(tableName));
            Result result = hTable.get(get);
            if (result == null || result.listCells() == null) {
                return null;
            }
            entity = transToEntity(result, T);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (T) entity;
    }

    /**
     * 对象使用了ORMHBaseTable 和 ORMHBaseColumn 注解
     * @param filterList
     * @param family
     * @param rowPrefix
     * @param pageDto
     * @param T
     * @return
     */
    public PageVo queryPage(FilterList filterList, String family, String rowPrefix, PageDto pageDto, Class<? extends BaseEntity> T) {
        long start = System.currentTimeMillis();

        ORMHBaseTable ormhBaseTable = T.getAnnotation(ORMHBaseTable.class);
        if(ormhBaseTable==null){
            throw new RuntimeException("该实体没有使用ORMHBaseTable注解");
        }
        String tableName=ormhBaseTable.tableName();
        if(StringUtils.isBlank(tableName)){
            throw new RuntimeException("请声明表名！");
        }

        Connection connection = ThreadShare.getConnection();
        PageVo pageVo = new PageVo();
        Integer pageSize = pageDto.getPageSize();
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }

        int total = 8888888;
        pageVo.setTotal(total);
        pageVo.setTotalPage(total / pageSize + ((total % pageSize) > 0 ? 1 : 0));
        Filter filter = new PageFilter(pageSize);//设置页数
        byte[] POSTFIX = new byte[]{0x00};
        List<BaseEntity> entities = new ArrayList<BaseEntity>();
        try {
            Scan scan = new Scan();
            scan.addFamily(family.getBytes());
            String startRowKey=pageDto.getStartRowKey();
            if (StringUtils.isNotBlank(pageDto.getStartRowKey())) {//设置开始rowKey
                byte[] startRow = Bytes.add(startRowKey.getBytes(), POSTFIX);//是当前行的下一行
                scan.setStartRow(startRow);
                pageVo.setPreRowKey(pageDto.getStartRowKey());//前一页的开始rowkey
            }else if(StringUtils.isNotBlank(rowPrefix)){
                scan.setRowPrefixFilter(rowPrefix.getBytes());
            }
            filterList.addFilter(filter);
            scan.setFilter(filterList);
            scan.setCaching(pageSize);//每一次RPC请求的记录数
            ResultScanner scanner = table.getScanner(scan);
            Result result;
            while ((result = scanner.next()) != null && (pageSize >= entities.size())) {
                BaseEntity entity = transToEntity(result, T);
                if (entity != null) {
                    entities.add(entity);
                }
            }
            pageVo.setDataList(entities);
            if (entities.size() >= pageSize) {
                BaseEntity entity = entities.get(entities.size() - 1);
                pageVo.setNextRowKey(entity.getId());//下一页的开始rowkey
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("分页查询耗时="+(System.currentTimeMillis()-start)/1000);
        return pageVo;
    }

    public List<? extends BaseEntity> list(FilterList filterList, String family, String rowPrefix,Class<? extends BaseEntity> T) {
        long start = System.currentTimeMillis();

        ORMHBaseTable ormhBaseTable = T.getAnnotation(ORMHBaseTable.class);
        if(ormhBaseTable==null){
            throw new RuntimeException("该实体没有使用ORMHBaseTable注解");
        }
        String tableName=ormhBaseTable.tableName();
        if(StringUtils.isBlank(tableName)){
            throw new RuntimeException("请声明表名！");
        }
        Connection connection = ThreadShare.getConnection();
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }

        List<BaseEntity> entities = new ArrayList<BaseEntity>();
        try {
            Scan scan = new Scan();
            scan.addFamily(family.getBytes());
           if(StringUtils.isNotBlank(rowPrefix)){
                scan.setRowPrefixFilter(rowPrefix.getBytes());
            }
            if(filterList.getFilters().size()>0){
                scan.setFilter(filterList);
            }
            scan.setCaching(10);//每一次RPC请求的记录数
            ResultScanner scanner = table.getScanner(scan);
            Result result;
            while ((result = scanner.next()) != null) {
                BaseEntity entity = transToEntity(result, T);
                if (entity != null) {
                    entities.add(entity);
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("分页查询耗时="+(System.currentTimeMillis()-start)/1000);
        return entities;
    }


    /**
     * 对象使用了ORMHBaseTable 和 ORMHBaseColumn 注解
     * @param result
     * @param T
     * @param <T>
     * @return
     */
    public <T> T transToEntity(Result result, Class<? extends BaseEntity> T) {
        BaseEntity baseEntity = null;
        Field[] fields = T.getDeclaredFields();
        Map<String,String> quaFieldMap=new HashMap<>();//列-属性 映射
        for (Field f:fields){
            String fieldName=f.getName();
            Annotation annotation = f.getAnnotation(ORMHBaseColumn.class);//获取指定注解
            if(annotation!=null){//有该类型的注解存在
                ORMHBaseColumn ormhBaseColumn= (ORMHBaseColumn) annotation;
                String qualifier = ormhBaseColumn.qualifier();//列
                quaFieldMap.put(qualifier,fieldName);
            }
        }
        try {
            baseEntity = T.newInstance();
            for (Cell cell : result.listCells()) {
                String qualifier = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                if (baseEntity.getId() == null) {
                    String rowKey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                    baseEntity.setId(rowKey);
                }
                String fieldName=quaFieldMap.get(qualifier);
                BeanUtils.setProperty(baseEntity, fieldName, Bytes.toString(CellUtil.cloneValue(cell)));
            }
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ((T) baseEntity);
    }

    public void delete(List<String> ids,String tableName) {
        Connection connection = ThreadShare.getConnection();
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            List<Delete> deletes=new ArrayList<>();
            for(String id:ids){
                Delete del = new Delete(id.getBytes());
                deletes.add(del);
            }
            table.delete(deletes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void delete(String id,String tableName) {
        List<String> ids=new ArrayList<>();
        ids.add(id);
        delete(ids,tableName);
    }
}
