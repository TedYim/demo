package com.example.demo.spring_event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by Ted on 2018/11/21.
 */
@Configuration
public class EventConfig {

    @Bean
    SimpleApplicationEventMulticaster getBean(ThreadPoolTaskExecutor taskExecutor) {
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.setTaskExecutor(taskExecutor);
        return multicaster;
    }
}
