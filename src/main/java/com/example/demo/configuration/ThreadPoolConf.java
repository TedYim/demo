package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池类
 */
@Configuration
public class ThreadPoolConf {

    /**
     * Spring封装线程池
     *
     * @return
     */
    @Bean("taskExecutor")
    ThreadPoolTaskExecutor genThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);//<!-- 核心线程数  -->
        executor.setMaxPoolSize(10);//<!-- 最大线程数 -->
        executor.setQueueCapacity(1000);//<!-- 队列最大长度 -->
        executor.setKeepAliveSeconds(200);//<!-- 线程池维护线程所允许的空闲时间 -->
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//<!-- 线程池对拒绝任务(无线程可用)的处理策略 -->
        return executor;
    }
}
