package com.example.demo.kafka.integrate.collection.util;

import java.io.IOException;

import com.example.demo.kafka.integrate.collection.hbase.HBaseConnectionPool;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;



public final class HBaseHelper {

    private Admin admin = null;

    private static HBaseHelper helper = new HBaseHelper();

    private HBaseHelper() {
        HBaseConnectionPool pool = HBaseConnectionPool.getConnectionPool();
        Connection connection = pool.getConnection();
        try {
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HBaseHelper getHBaseHelper() {
        return helper;
    }

    public boolean existsTable(String table) throws IOException {
        return existsTable(TableName.valueOf(table));
    }

    public boolean existsTable(TableName table) throws IOException {
        return admin.tableExists(table);
    }

    public void createTable(String table, String... colfams) throws IOException {
        createTable(TableName.valueOf(table), 1, null, colfams);
    }

    public void createTable(TableName table, String... colfams)
            throws IOException {
        createTable(table, 1, null, colfams);
    }

    public void createTable(String table, int maxVersions, String... colfams)
            throws IOException {
        createTable(TableName.valueOf(table), maxVersions, null, colfams);
    }

    public void createTable(TableName table, int maxVersions, String... colfams)
            throws IOException {
        createTable(table, maxVersions, null, colfams);
    }

    public void createTable(String table, byte[][] splitKeys, String... colfams)
            throws IOException {
        createTable(TableName.valueOf(table), 1, splitKeys, colfams);
    }

    public void createTable(TableName table, int maxVersions,
                            byte[][] splitKeys, String... colfams) throws IOException {
        HTableDescriptor desc = new HTableDescriptor(table);
        for (String cf : colfams) {
            HColumnDescriptor coldef = new HColumnDescriptor(cf);
            coldef.setMaxVersions(maxVersions);
            desc.addFamily(coldef);
        }
        desc.addCoprocessor("org.apache.hadoop.hbase.coprocessor.AggregateImplementation");
        if (splitKeys != null) {
            admin.createTable(desc, splitKeys);
        } else {
            admin.createTable(desc);
        }
    }

    public void addFamily(TableName table, String... colfams) throws IOException {
        for (String cf : colfams) {
            HColumnDescriptor coldef = new HColumnDescriptor(cf);
            coldef.setMaxVersions(1);
            admin.addColumn(table, coldef);
        }
    }

    public void disableTable(String table) throws IOException {
        disableTable(TableName.valueOf(table));
    }

    public void enableTable(String table) throws IOException {
        admin.enableTable(TableName.valueOf(table));
    }

    public void disableTable(TableName table) throws IOException {
        admin.disableTable(table);
    }

    public void dropTable(String table) throws IOException {
        dropTable(TableName.valueOf(table));
    }

    public void dropTable(TableName table) throws IOException {
        if (existsTable(table)) {
            if (admin.isTableEnabled(table))
                disableTable(table);
            admin.deleteTable(table);
        }
    }

}
