package com.example.demo.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存的配置信息
 */
@Configuration
public class CacheConf {

    public static final int DEFAULT_MAXSIZE = 50000;
    public static final int DEFAULT_TTL     = 10;

    /**
     * 定義cache名稱、超時時長（秒）、最大size
     * 每个cache缺省10秒超时、最多缓存50000条数据，需要修改可以在构造方法的参数中指定。
     */
    public enum Caches {
        messageContent(8640000),
        getSomeData,
        qiniuUpToken(1800, 1),

        getCommonAds(60),
        getAndAssembleAreaSpecificAds(60),
        guavaCache(60);

        Caches() {
        }

        Caches(int ttl) {
            this.ttl = ttl;
        }

        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }

        private int maxSize = DEFAULT_MAXSIZE;    //最大數量
        private int ttl     = DEFAULT_TTL;        //过期时间（秒）

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        public int getTtl() {
            return ttl;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }
    }


    /**
     * 创建基于guava的Cache Manager
     *
     * @return
     */
    @Bean
    public SimpleCacheManager guavaCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        //把各个cache注册到cacheManager中，GuavaCache实现了org.springframework.cache.Cache接口
        ArrayList<org.springframework.cache.Cache> caches = new ArrayList<>();
        for (Caches c : Caches.values()) {
            caches.add(new GuavaCache(c.name(), CacheBuilder.newBuilder().recordStats().expireAfterWrite(c.getTtl(), TimeUnit.SECONDS).maximumSize(c.getMaxSize()).build()));
        }
        cacheManager.setCaches(caches);
        CaffeineCache caffeineCache = new CaffeineCache("caffeineCache", Caffeine.newBuilder().recordStats()
                .expireAfterWrite(20, TimeUnit.SECONDS)
                .maximumSize(50000)
                .build());
        caches.add(caffeineCache);
        cacheManager.setCaches(caches);
        System.out.println("初始化缓存结束 : " + caches);
        return cacheManager;
    }

    @Bean
    CacheManager redisCacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>(16);
        // create "redissonCache" cache with ttl(过期时间) = 24 minutes and maxIdleTime(连接的最大空闲时间,如果超过这个时间,某个数据库连接还没有被使用,则会断开掉这个连接。) = 12 minutes
        config.put("redissonCache", new CacheConfig(24 * 60 * 1000, 12 * 60 * 1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }


    @Bean
    CacheManager ehcacheCacheManager() {
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
        net.sf.ehcache.CacheManager ehcacheManager;
        //创建底层Cache
        try {
            ehcacheManager = new net.sf.ehcache.CacheManager(new ClassPathResource("ehcache.xml").getInputStream());
            ehCacheCacheManager.setCacheManager(ehcacheManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ehCacheCacheManager;
    }

    /**
     * 测试CompositeCacheManager
     */
    @Bean
    @Primary
    CompositeCacheManager compositeCacheManager(RedissonClient redissonClient) {
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        ArrayList<CacheManager> cacheManagers = Lists.newArrayList();
        cacheManagers.add(ehcacheCacheManager());
        cacheManagers.add(guavaCacheManager());
        cacheManagers.add(redisCacheManager(redissonClient));
        compositeCacheManager.setCacheManagers(cacheManagers);
        return compositeCacheManager;
    }


}
