package com.example.demo.dynamic_data_source;

public final class DynamicDataSourceHolder {

    private static final ThreadLocal<DynamicDataSourceEnum> holder = new ThreadLocal<>();

    private DynamicDataSourceHolder() {
    }

    public static void putDataSource(DynamicDataSourceEnum dataSource){
        holder.set(dataSource);
    }

    public static DynamicDataSourceEnum getDataSource(){
        return holder.get();
    }

    public static void clearDataSource() {
        holder.remove();
    }

}
