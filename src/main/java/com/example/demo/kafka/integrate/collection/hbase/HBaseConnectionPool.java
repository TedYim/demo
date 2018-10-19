package com.example.demo.kafka.integrate.collection.hbase;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public final class HBaseConnectionPool {

    private String hbaseMaster = "";
    private String zookeeper = "";
    Vector<Connection> connections = new Vector<>();

    public static Configuration conf = HBaseConfiguration.create();

    private int initPollNumber = 20;

    private static HBaseConnectionPool connectionPool = new HBaseConnectionPool();

    public static void init(String hbaseMaster, String zookeeper) {
        connectionPool.hbaseMaster = hbaseMaster;
        connectionPool.zookeeper = zookeeper;
        connectionPool.init();
    }

    private HBaseConnectionPool() {

    }

    private void init() {
        initConfiguration();
        initConnection();
        new ProduceConnectionTask();
    }

    private void initConfiguration() {
        conf.set("hbase.master", hbaseMaster);
        conf.set("hbase.zookeeper.property.clientport", "2181");
        conf.set("hbase.zookeeper.quorum", zookeeper);
        // 提高RPC通信时长
        conf.setLong("hbase.rpc.timeout", 600000);
        // 设置Scan缓存
        conf.setLong("hbase.client.scanner.caching", 1000);
    }

    class ProduceConnectionTask implements Runnable {
        public ProduceConnectionTask() {
            Thread thread = new Thread();
            thread.setDaemon(true);
            thread.start();
        }

        public void run() {
            while (true) {
                try {
                    if (connections.size() < initPollNumber / 2) {
                        initConnection();
                    }
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void initConnection() {
        try {
            for (int i = 0; i < initPollNumber; i++) {
                connections.add(createConnection());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private Connection createConnection() throws IOException {
        return ConnectionFactory.createConnection(conf);
    }

    public Connection getConnection() {
        Connection connection = null;
        if (connections.size() > 0) {
            connection = connections.remove(0);
        } else {
            initConnection();
            connection = connections.remove(0);
        }
        return connection;
    }

    public static HBaseConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
