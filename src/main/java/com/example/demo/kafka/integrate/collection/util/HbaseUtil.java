package com.example.demo.kafka.integrate.collection.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.topscore.integrate.annotation.ORMHBaseColumn;


public class HbaseUtil {

    /**
     * HBase result 转换为 bean
     *
     * @param <T>
     * @param result
     * @param obj
     * @return
     * @throws Exception
     */
    public static <T> T result2Bean(Result result, T obj) throws Exception {
        if (result == null) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ORMHBaseColumn.class)) {
                continue;
            }
            ORMHBaseColumn orm = field.getAnnotation(ORMHBaseColumn.class);
            String f = orm.family();
            String q = orm.qualifier();
            boolean timeStamp = orm.timestamp();
            if (StringUtils.isBlank(f) || StringUtils.isBlank(q)) {
                continue;
            }
            String fieldName = field.getName();
            String value = "";
            if (f.equalsIgnoreCase("rowkey")) {
                value = new String(result.getRow());
            } else {
                value = getResultValueByType(result, f, q, timeStamp);
            }
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String setMethodName = "set" + firstLetter + fieldName.substring(1);
            Method setMethod = clazz.getMethod(setMethodName, new Class[]{field.getType()});
            setMethod.invoke(obj, new Object[]{value});
        }
        return obj;
    }

    private static String getResultValueByType(Result result, String family, String qualifier, boolean timeStamp) {
        if (!timeStamp) {
            String val = "";
            if (result.getValue(Bytes.toBytes(family), Bytes.toBytes(qualifier)) != null) {
                val = new String(result.getValue(Bytes.toBytes(family), Bytes.toBytes(qualifier)));
            }
            return val;
        }
        List<Cell> cells = result.getColumnCells(Bytes.toBytes(family), Bytes.toBytes(qualifier));
        if (cells.size() == 1) {
            Cell cell = cells.get(0);
            return cell.getTimestamp() + "";
        }
        return "";
    }


    public static void main(String[] args) throws IOException {
        System.setProperty("hadoop.home.dir", "");
        System.setProperty("HADOOP_HOME", "");
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHBaseHelper();
        helper.dropTable("testtable");
//		    helper.createTable("testtable", "colfam1");
//		    // vv PutWriteBufferExample1
//		    TableName name = TableName.valueOf("testtable");
//		    Connection connection = ConnectionFactory.createConnection(conf);
//		    Table table = connection.getTable(name);
//		    BufferedMutator mutator = connection.getBufferedMutator(name); // co PutWriteBufferExample1-1-CheckFlush Get a mutator instance for the table.
//
//		    Put put1 = new Put(Bytes.toBytes("row1"));
//		    put1.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"),
//		      Bytes.toBytes("val1"));
//		    mutator.mutate(put1); // co PutWriteBufferExample1-2-DoPut Store some rows with columns into HBase.
//
//		    Put put2 = new Put(Bytes.toBytes("row2"));
//		    put2.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"),
//		      Bytes.toBytes("val2"));
//		    mutator.mutate(put2);
//
//		    Put put3 = new Put(Bytes.toBytes("row3"));
//		    put3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"),
//		      Bytes.toBytes("val3"));
//		    mutator.mutate(put3);
//
//		    Get get = new Get(Bytes.toBytes("row1"));
//		    Result res1 = table.get(get);
//		    System.out.println("Result: " + res1); // co PutWriteBufferExample1-3-Get1 Try to load previously stored row, this will print "Result: keyvalues=NONE".
//
//		    mutator.flush(); // co PutWriteBufferExample1-4-Flush Force a flush, this causes an RPC to occur.
//
//		    Result res2 = table.get(get);
//		    System.out.println("Result: " + res2); // co PutWriteBufferExample1-5-Get2 Now the row is persisted and can be loaded.
//
//		    mutator.close();
//		    table.close();
//		    connection.close();
        // ^^ PutWriteBufferExample1
    }
}
