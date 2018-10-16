package com.example.demo.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 测试zk版本与临时节点的关系
 */
public class CuratorTest {


    public static void main(String[] args) throws Exception {
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

}
