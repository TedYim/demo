package com.example.demo.lock.tools;

import com.example.demo.lock.constants.ExpireTimeConstant;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RedisTools {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";// – 设置键key的过期时间，单位时毫秒
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 根据redisTemplate获取jedis实例
     *
     * @param redisTemplate
     * @return
     */
    public static Jedis getJedis(RedisTemplate redisTemplate) {
        Field poolField = ReflectionUtils.findField(JedisConnectionFactory.class, "pool");
        ReflectionUtils.makeAccessible(poolField);
        Pool<Jedis> pool = (Pool<Jedis>) ReflectionUtils.getField(poolField, redisTemplate.getConnectionFactory());
        return pool.getResource();
    }

    /**
     * 尝试获取分布式锁
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间  默认10分钟
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        if (expireTime <= 0) {
            expireTime = ExpireTimeConstant.TEN_MIN;
        }
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 尝试获取分布式锁
     *
     * @param redisTemplate
     * @param lockKey       锁
     * @param requestId     请求标识
     * @param expireTime    超期时间  默认10分钟
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(RedisTemplate redisTemplate, String lockKey, String requestId, int expireTime) {
        Jedis jedis = getJedis(redisTemplate);
        boolean result;
        try {
            result = tryGetDistributedLock(jedis, lockKey, requestId, expireTime);
        } finally {
            jedis.close();//放回池中
        }
        return result;
    }

    /**
     * 尝试获取分布式锁
     *
     * @param redisTemplate
     * @param lockMap       一组锁
     * @param expireTime    超期时间  默认10分钟ap
     * @return 是否获取成功
     */
    public static Map<String, Boolean> tryGetDistributedLock(RedisTemplate redisTemplate, Map<String, String> lockMap, int expireTime) {
        Map<String, Boolean> resultMap = new HashMap<>(lockMap.size());
        if (!lockMap.isEmpty()) {
            Jedis jedis = getJedis(redisTemplate);
            try {
                for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                    boolean isSuccess = tryGetDistributedLock(jedis, entry.getKey(), entry.getValue(), expireTime);
                    resultMap.put(entry.getKey(), isSuccess);
                }
            } finally {
                jedis.close();
            }
        }
        return resultMap;
    }

    /**
     * 释放分布式锁
     *
     * @param jedis     Redis客户端
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param redisTemplate
     * @param lockKey       锁
     * @param requestId     请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(RedisTemplate redisTemplate, String lockKey, String requestId) {

        Jedis jedis = getJedis(redisTemplate);
        boolean result;
        try {
            result = releaseDistributedLock(jedis, lockKey, requestId);
        } finally {
            jedis.close();//放回池中
        }
        return result;
    }

    /**
     * 释放分布式锁
     *
     * @param redisTemplate
     * @param lockMap   一组锁
     */
    public static Map<String, Boolean> releaseDistributedLock(RedisTemplate redisTemplate, Map<String, String> lockMap) {
        Map<String, Boolean> resultMap = new HashMap<>(lockMap.size());
        if (!lockMap.isEmpty()) {
            Jedis jedis = getJedis(redisTemplate);
            try {
                for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                    boolean isSuccess = releaseDistributedLock(redisTemplate, entry.getKey(), entry.getValue());
                    resultMap.put(entry.getKey(), isSuccess);
                }
            } finally {
                jedis.close();
            }
        }
        return resultMap;
    }
}
