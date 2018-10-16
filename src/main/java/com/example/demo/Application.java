package com.example.demo;

import com.example.demo.configuration.ServerConf;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


@EnableCaching //加上这个注解是的支持缓存注解
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.example.demo"})
//@MapperScan(basePackages = "com.topscore.uke.*.mapper")
@Import(value = {ServerConf.class})
@ImportResource(locations = {"classpath:spring/spring-threadpool.xml"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}




