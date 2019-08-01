package com.example.demo.mybatis.page;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

@Intercepts({
        @Signature(type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class}
        )
})
public class PagingPlugin implements Interceptor {

    private Integer defaultPage;//默认页码
    private Integer defaultPageSize;//默认每页条数
    private Boolean defaultUserFlag;//默认是否启动插件

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler stmtHandler = getUnProxyObject(invocation);
        MetaObject metaObject = SystemMetaObject.forObject(stmtHandler);
        String sql = (String) metaObject.getValue("delegate.boundSql.sql");
        //不是select 语句
        if (!checkSelect(sql)) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Object parameterObject = boundSql.getParameterObject();
        PageParams pageParams = getPageParams(parameterObject);
        if (pageParams == null) {//没有分页参数，不启动插件
            return invocation.proceed();
        }
        //获取分页参数，获取不到时候，使用默认值

        Integer pageNum = pageParams.getPage() == null ? this.defaultPage : pageParams.getPage();
        Integer pageSize = pageParams.getPageSize() == null ? this.defaultPageSize : pageParams.getPageSize();
        Boolean userFlag = pageParams.getUserFlag() == null ? this.defaultUserFlag : pageParams.getUserFlag();
        if (!userFlag) {//不使用分页插件
            return invocation.proceed();
        }
        int total = getTotal(invocation, metaObject, boundSql);
        //回填总数到分页参数里
        setTotalToPageParams(pageParams, total, pageSize);
        //修改SQL
        return changeSQL(invocation, metaObject, boundSql, pageNum, pageSize);
    }

    /***
     * 判断是否select语句
     */
    private boolean checkSelect(String sql) {
        String trimSQl = sql.trim();
        int idx = trimSQl.toLowerCase().indexOf("select");
        return idx == 0;
    }

    /***
     * getUnProxyObject:从代理对象中分离出真实对象
     */
    private StatementHandler getUnProxyObject(Invocation ivt) {
        StatementHandler statementHandler = (StatementHandler) ivt.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        //分离代理对象链（由于目标类可能被多个拦截器拦截，从而形成多次代理，通过循环可以分离出最原始的目标类。
        Object object = null;
        while (metaObject.hasGetter("h")) {
            object = metaObject.getValue("h");
        }
        if (object == null) {
            return statementHandler;
        }
        return (StatementHandler) object;
    }

    @Override
    public Object plugin(Object statementHander) {
        return Plugin.wrap(statementHander, this);
    }

    @Override
    public void setProperties(Properties props) {
        String strDefaultPage = props.getProperty("default.page", "1");//默认页码
        String strDefaultPageSize = props.getProperty("default.pageSize", "20");//默认每页条数
        String strDefaultUserFlag = props.getProperty("default.userFlag", "1");
        ;//默认是否启动插件
        this.defaultPage = Integer.parseInt(strDefaultPage);
        this.defaultPageSize = Integer.parseInt(strDefaultPageSize);
        this.defaultUserFlag = Boolean.parseBoolean(strDefaultUserFlag);
    }

    /**
     * 分解分页参数，这里支持使用Map和Param注解传递参数，或者POJO继承PageParams这三种方式。
     *
     * @param parameterObject --sql允许参数
     */
    @SuppressWarnings("unchecked")
    private PageParams getPageParams(Object parameterObject) {
        if (parameterObject == null) {
            return null;
        }
        PageParams pageParams = null;
        if (parameterObject instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>) parameterObject;
            Set<Entry<String, Object>> entries = paramMap.entrySet();
            for (Entry<String, Object> entry : entries) {
                if (entry.getValue() instanceof PageParams) {
                    pageParams = (PageParams) entry.getValue();
                    break;
                }
            }

        } else if (parameterObject instanceof PageParams) {
            pageParams = (PageParams) parameterObject;
        }

        return pageParams;
    }


    String countSql(String sql) {
        String line = null;
        StringBuffer buffer = new StringBuffer(" select count(1) as total ");
        boolean found = false;
        int index = 0;
        StringReader reader = new StringReader(sql);
        BufferedReader bufferedReader = new BufferedReader(reader);
        try {
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (((index = line.toLowerCase().indexOf("from"))) > -1 && found == false) {
                    line = line.substring(index, line.length());
                    found = true;
                }
                if (line.trim().toLowerCase().indexOf("group") > -1) {
                    System.out.println("sql================" + line);
                    return "select count(*) as total from (" + sql + ") $_paging";
                }
                if (found) {

                    buffer.append(" ").append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    /****
     * getTotal:(这里用一句话描述这个方法的作用).
     */
    private int getTotal(Invocation ivt, MetaObject metaStatementHandler, BoundSql boundSql) throws Throwable {
        //获取当前的mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        //配置对象
        Configuration cfg = mappedStatement.getConfiguration();
        //当前需要执行的SQL
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        //改写为统计总数的SQL，这里是MySQL数据库，如果是其他的数据库，需要按照数据库的SQL规范改写

        String countSql = countSql(sql);

        //获取拦截方法参数，我们知道是Connection对象
        Connection connection = (Connection) ivt.getArgs()[0];
        PreparedStatement ps = null;
        int total = 0;
        try {
            //预编译统计总数SQL
            ps = connection.prepareStatement(countSql);
            //构建统计总数BoundSql
            BoundSql countBoundSql = new BoundSql(cfg, countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            //构建MyBatis的ParameterHandler用来设置总数SQL的参数
            ParameterHandler handler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), countBoundSql);
            //设置总数SQL参数
            handler.setParameters(ps);
            //执行查询
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("total");
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return total;
    }

    /***
     * setTotalToPageParams:(这里用一句话描述这个方法的作用).
     */
    private void setTotalToPageParams(PageParams pageParams, int total, int pageSize) {
        int totalPage = 0;

        totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;

        pageParams.setTotal(total);
        pageParams.setPageSize(pageSize);
        pageParams.setTotalPage(totalPage);
    }

    public Object changeSQL(Invocation invocation, MetaObject metaStatementHandler, BoundSql boundSql, int page, int pageSize) throws Exception {
        //获取当前需要执行的SQL
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        //修改SQl，这里使用的是MySQL，如果是其他数据库则需要修改.
        String newSql = sql + " limit ?,?";
        //修改当前需要执行的SQL
        metaStatementHandler.setValue("delegate.boundSql.sql", newSql);
        //相当于调用StatementHandler的prepage方法，预编译了当前SQL并设置原有的参数，但是少了两个分页参数，它返回的是一个PreparedStatement对象
        PreparedStatement ps = (PreparedStatement) invocation.proceed();
        //计算SQL的总参数个数
        int count = ps.getParameterMetaData().getParameterCount();
        //设置两个分页参数
        ps.setInt(count - 1, (page - 1) * pageSize);
        ps.setInt(count, pageSize);
        return ps;
    }

}
