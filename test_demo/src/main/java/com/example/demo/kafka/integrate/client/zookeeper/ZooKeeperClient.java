package com.example.demo.kafka.integrate.client.zookeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperClient {


    public static final int MAX_RETRIES = 3;

    public static final int BASE_SLEEP_TIMEMS = 3000;

    public String nameSpace = "kafka-config";

    private CuratorFramework client;

    public CuratorFramework get() {
        return client;
    }

    public ZooKeeperClient(String zookeeperUrl) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIMEMS, MAX_RETRIES);
        client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperUrl)
                .retryPolicy(retryPolicy)
                .namespace(nameSpace)
                .build();
        client.start();
    }

    /***
     *
     * @param path
     * @return
     * @throws Exception
     */
    public String getDataNode(String path) {
        try {
            byte[] datas = client.getData().forPath("/" + path);
            return new String(datas);
        } catch (Exception e) {
            return "";
        }
    }

    public void setDataNode(String path, String data) {
        try {
            client.setData().forPath("/" + path, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     *
     * @param path
     * @return
     * @throws Exception
     */
    public void removeNode(String path) throws Exception {
        Stat stat = client.checkExists().forPath("/" + path);
        if (stat != null) {
            client.delete().forPath("/" + path);
        }
    }

    /***
     *
     * @param path
     * @return
     * @throws Exception
     */
    public List<String> getChildrenNode(String path) {
        List<String> dataList = new ArrayList<String>();
        try {
            List<String> paths = client.getChildren().forPath("/" + path);
            for (String p : paths) {
                if (p != null && p.length() > 0) {
                    dataList.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /***
     *
     * @param path
     * @return
     * @throws Exception
     */
    public Map<String, Integer> getChildrenNodeAndData(String path) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        try {
            List<String> paths = client.getChildren().forPath("/" + path);
            for (String p : paths) {
                if (p != null && p.length() > 0) {
                    String data = getDataNode(p);
                    if (data.trim().length() == 0) {
                        map.put(p, 1);
                    } else {
                        map.put(p, Integer.valueOf(data));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
