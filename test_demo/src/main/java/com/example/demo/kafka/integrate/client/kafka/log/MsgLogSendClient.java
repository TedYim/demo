package com.example.demo.kafka.integrate.client.kafka.log;

import com.example.demo.kafka.integrate.client.zookeeper.ZooKeeperClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.*;
import java.util.Map.Entry;


class MsgLogSendClient {
    static String serverPath = "kafka-collection-servers";
    Channel channel;
    static ZooKeeperClient keeperClient = null;
    String  host;
    int     port;
    Boolean connected = null;
    static Object lock = new Object();

    public MsgLogSendClient(String zookeeperUrl) {
        synchronized (lock) {
            if (keeperClient == null) {
                keeperClient = new ZooKeeperClient(zookeeperUrl);
            }
            new Thread(new CheckChannelTask()).start();
            init();
        }
    }

    private void init() {
        Map<String, Integer> map = keeperClient.getChildrenNodeAndData(serverPath);
        List<Map.Entry<String, Integer>> entrys = new ArrayList<Entry<String, Integer>>(map.entrySet());
        Collections.sort(entrys, Comparator.comparing(Map.Entry::getValue));

        String newServer = null;
        String[] temp = null;
        Integer connectedCount = 0;
        if (entrys.size() > 0) {
            Map.Entry<String, Integer> entry = entrys.get(0);
            newServer = entry.getKey();
            connectedCount = entry.getValue();
        }
        if (newServer != null && (temp = newServer.split(":")).length == 2) {
            this.host = temp[0];
            this.port = Integer.valueOf(temp[1]);
            connectionServer();
            keeperClient.setDataNode(serverPath + "/" + newServer, String.valueOf(++connectedCount));
        }
    }

    private class CheckChannelTask implements Runnable {
        private long sleepTime = 1000 * 60;

        @Override
        public void run() {
            while (true) {
                try {
                    if (connected != null && !connected) {
                        failover();
                    }
                    Thread.sleep(sleepTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void connectionServer() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .channel(NioSocketChannel.class)
                    .handler(new MsgLogClientInitializer(this));
            channel = bootstrap.connect(host, port).sync().channel();
            connected = true;
        } catch (Exception e) {
            failover();
        }
    }

    public void failover() {
        connected = false;
        init();

    }

    public void send(String line) {
        if (line != null && line.trim().length() > 0 && channel != null && connected) {
            channel.writeAndFlush(line + LogConstant.delimiter);
        }
    }
}
