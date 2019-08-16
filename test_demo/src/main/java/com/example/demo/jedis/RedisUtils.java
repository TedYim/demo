package com.example.demo.jedis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getValue(String key) {
        final String k = key;
        Object obj = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] bs = connection.get(k.getBytes());
                if (bs != null) {
                    return new String(bs);
                }
                return null;
            }
        });

        if (obj != null) {
            return obj.toString();
        }

        return null;
    }

    public void setValue(String key, String value) {
        final String k = key;
        final String v = value;
        redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(k.getBytes(), v.getBytes());
                return null;
            }
        });
    }

    public Boolean setNX(String key, String value) {
        final String k = key;
        final String v = value;
        Boolean res = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                Boolean b = connection.setNX(k.getBytes(), v.getBytes());
                return b;
            }
        });
        return res;
    }

    public void delValue(String key) {
        final String k = key;
        redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(k.getBytes());
                return null;
            }
        });
    }

    public void expire(String key, long seconds) {
        final String k = key;
        final long v = seconds;
        redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.expire(k.getBytes(), v);
                return null;
            }
        });
    }


}
