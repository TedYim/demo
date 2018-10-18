package com.example.demo.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.nodes.PersistentNode;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

/**
 * 测试zk版本与临时节点的关系
 */
public class CuratorNodeDemo {

    /**
     * 测试Curator版本与分布式锁建立的节点类型的关系
     * Container Nodes – 容器节点 zookeeper在3.6版本后加入容器节点的概念。
     * 容器节点是有特别目的的节点，当作为leader 锁是很有用。
     * 但是在3.6版本之前Curator使用Sequence Nodes来代替容器节点
     */
    public static void ephemeralNodesVersionTest() throws Exception {
        ExponentialBackoffRetry ExponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
                //"172.16.37.87:2181",
                "127.0.0.1:2181",
                5000,
                3000,
                ExponentialBackoffRetry);
        curatorFramework.start();
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, "/yzytest/test1");
        lock.acquire();

        Thread.sleep(Integer.MAX_VALUE);
    }


    /**
     * 测试Curator临时节点的创建
     * 保护方式是指一种很边缘的情况：当服务器将节点创建好，但是节点名还没有返回给client,这时候服务器可能崩溃了，然后此时ZK session仍然合法，所以此临时节点不会被删除。对于client来说，它无法知道哪个节点是它们创建的。
     * 即使不是sequential-ephemeral,也可能服务器创建成功但是客户端由于某些原因不知道创建的节点。
     * Curator对这些可能无人看管的节点提供了保护机制。 这些节点创建时会加上一个GUID。 如果节点创建失败正常的重试机制会发生。 重试时， 首先搜索父path, 根据GUID搜索节点，如果找到这样的节点， 则认为这些节点是第一次尝试创建时创建成功但丢失的节点，然后返回给调用者。
     * 注意：节点必须调用start方法启动。 不用时调用close方法。PersistentEphemeralNode 内部自己处理错误状态。
     *
     * Ephemeral Nodes – 临时结点 Zookeeper还有一个临时节点的概念。
     * 这些节点在创建这些节点的session的活动周期中存货。当session结束，这些节点也被删除了。
     * 因此。临时节点不允许有子节点。
     * 如果创建时有诸如/example/node12此类的路径,那么父节点以永久节点的型式创建
     *
     */
    public static void ephemeralNodesCreateTest() throws Exception {
        final String PATH = "/example2/ephemeralNode";
        final String PATH2 = "/example/node3";
        ExponentialBackoffRetry ExponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "127.0.0.1:2181",
                5000,
                3000,
                ExponentialBackoffRetry);

        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("连接状态:" + newState.name());
            }
        });

        client.start();

        PersistentNode node = new PersistentNode(client, CreateMode.EPHEMERAL, true, PATH, "临时节点".getBytes());
        node.start();
        node.waitForInitialCreate(3, TimeUnit.SECONDS);
        String actualPath = node.getActualPath();
        System.out.println("临时节点路径：" + actualPath + " | 值: " + new String(client.getData().forPath(actualPath)));
        client.create().forPath(PATH2, "持久化节点".getBytes());
        System.out.println("持久化节点路径： " + PATH2 + " | 值: " + new String(client.getData().forPath(PATH2)));
        Thread.sleep(Integer.MAX_VALUE);
    }


    public static void main(String[] args) throws Exception {
        CuratorNodeDemo.ephemeralNodesCreateTest();
    }

}
