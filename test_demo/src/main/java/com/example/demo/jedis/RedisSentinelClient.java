package com.example.demo.jedis;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.List;
import java.util.Set;


/**
 * 测试哨兵连接Sentinel集群
 * 显然，经过故障转移后，主从结构已经发生了改变且主已经死亡，如果还按照之前那样写死IP的方式连接Redis的话，势必会出现错误。
 * 可以想到，在Sentinel结构下，你必须向哨兵询问来获取谁是Master。
 */
public class RedisSentinelClient {

    public static void main(String[] args) {
        //10.10.0.31:26379;10.10.0.90:26379;10.10.0.64:26379
        Set<String> sentinels = Sets.newHashSet();
        sentinels.add(new HostAndPort("10.10.0.64", 26379).toString());
        sentinels.add(new HostAndPort("10.10.0.90", 26379).toString());
        sentinels.add(new HostAndPort("10.10.0.31", 26379).toString());
        JedisSentinelPool sentinelPool = new JedisSentinelPool("redis-master", sentinels, getJedisPoolConfig(), "oQHQYdWi5jg/6o7J/g==");
        System.out.println("Current master: " + sentinelPool.getCurrentHostMaster().toString());

        Jedis master = sentinelPool.getResource();
        master.set("username", "李佳薇");
        //sentinelPool.returnResource(master);
        List<Jedis> jedisList = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            Jedis masteri = sentinelPool.getResource();
            jedisList.add(masteri);
        }

        Jedis master2 = sentinelPool.getResource();
        Jedis master3 = sentinelPool.getResource();
        Jedis master4 = sentinelPool.getResource();
        Jedis master5 = sentinelPool.getResource();
        String value = master2.get("username");
        System.out.println("username: " + value);
        master2.close();
        sentinelPool.destroy();
    }


    public static JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(-1);//控制最多能创建多少连接,-1代表无限
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setMaxWaitMillis(1000);
        return jedisPoolConfig;
    }

}
