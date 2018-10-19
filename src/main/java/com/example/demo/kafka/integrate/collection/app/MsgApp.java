package com.example.demo.kafka.integrate.collection.app;


import com.example.demo.kafka.integrate.collection.hbase.HBaseConnectionPool;
import com.example.demo.kafka.integrate.collection.server.MsgServer;
import com.example.demo.kafka.integrate.collection.util.NetUtil;
import com.example.demo.kafka.integrate.collection.zookeeper.ZooKeeperClient;

public class MsgApp {

    static String serverPath = "kafka-collection-servers/";

    static {
        HBaseConnectionPool.init("172.16.35.57:60000", "172.16.34.200,172.16.34.201,172.16.34.202");
    }

    public static void main(String[] args) throws Exception {
        int port;
        String zookeeperUrl = "";
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
            zookeeperUrl = args[1];
        } else {
            port = 3537;
            zookeeperUrl = "172.16.34.200:2181";
        }
        registerServer(zookeeperUrl, port);
        new MsgServer(port).run();
        System.out.println("masApp start...");
    }

    static void registerServer(String zookeeperUrl, int port) throws Exception {
        ZooKeeperClient client = new ZooKeeperClient(zookeeperUrl);
        String host = NetUtil.getLoacalHost() + ":" + port;
        client.createPersistentEphemeralNode(serverPath + host, "1");
    }

}
