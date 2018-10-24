package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class TestFun {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RedisTemplate<String, Object> template;

    public void test() {
        RedisConnection connection = redisConnectionFactory.getConnection();
        template.opsForSet().add("set", "1");
        template.opsForSet().add("set", "2");
        template.opsForSet().add("set", "3");
        BoundSetOperations<String, Object> set = template.boundSetOps("set");
        System.out.println(set);
        template.delete("set");
        set = template.boundSetOps("set");
        System.out.println(set);
    }

    public static void main(String[] args) {
    }

}
