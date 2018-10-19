package com.example.demo.kafka.integrate.collection.zookeeper;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode.Mode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
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
     * @param client
     * @param path
     * @return
     * @throws Exception
     */
    public String  getDataNode(String path) throws Exception{  
    	try{
    		 byte[] datas = client.getData().forPath("/"+path);  
    		   return new String(datas);
    	}catch(Exception e){
    		return "";
    	}
    }  
    /***
     * 
     * @param client
     * @param path
     * @return
     * @throws Exception
     */
    public void  createNodeNode(String path) throws Exception{  
    	if(StringUtils.isBlank(getDataNode(path))){
    		client.create().creatingParentsIfNeeded().forPath("/"+path);
    	}
    }  
    public void  createPersistentEphemeralNode(String path,String value) throws Exception{ 
    	if(StringUtils.isBlank(getDataNode(path))){
    		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/"+path,value.getBytes());
    	}
    	
    }
    
  
    
    
}
