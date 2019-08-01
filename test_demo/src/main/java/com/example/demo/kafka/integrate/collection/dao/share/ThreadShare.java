package com.example.demo.kafka.integrate.collection.dao.share;

import org.apache.hadoop.hbase.client.Connection;

public class ThreadShare {
    private static ThreadLocal<Connection> connectionLocal = new ThreadLocal<Connection>();

    public static Connection getConnection() {
        return connectionLocal.get();
    }

    public static void setConnection(Connection connection) {
        connectionLocal.set(connection);
    }
}
