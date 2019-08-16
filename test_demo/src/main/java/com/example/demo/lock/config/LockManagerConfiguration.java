package com.example.demo.lock.config;

import com.example.demo.lock.tools.LockManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

public class LockManagerConfiguration {

    @Bean
    public LockManager lockManager(RedisTemplate redisTemplate){
        return new LockManager(redisTemplate);
    }
}
