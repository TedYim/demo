package com.example.demo.dynamic_data_source;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private Object writeDataSource; //写数据源

    private Object readDataSource; //读数据源

    @Override
    public void afterPropertiesSet() {
        if (this.writeDataSource == null) {
            throw new IllegalArgumentException("Property 'writeDataSource' is required");
        }
        setDefaultTargetDataSource(writeDataSource);
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DynamicDataSourceEnum.WRITE.name(), writeDataSource);
        if (readDataSource != null) {
            targetDataSources.put(DynamicDataSourceEnum.READ.name(), readDataSource);
        }
        setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {

        DynamicDataSourceEnum dynamicDataSourceGlobal = DynamicDataSourceHolder.getDataSource();

        if (dynamicDataSourceGlobal == null || dynamicDataSourceGlobal == DynamicDataSourceEnum.WRITE) {
            return DynamicDataSourceEnum.WRITE.name();
        }

        return DynamicDataSourceEnum.READ.name();
    }

    public void setWriteDataSource(Object writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public Object getWriteDataSource() {
        return writeDataSource;
    }

    public Object getReadDataSource() {
        return readDataSource;
    }

    public void setReadDataSource(Object readDataSource) {
        this.readDataSource = readDataSource;
    }
}
