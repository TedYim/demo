package com.demo;

import com.demo.health.MyHealthCheckHandler;
import com.demo.health.MyHealthIndicator;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringCloudApplication
public class TimeServiceApplication {

//    @Bean
//    public MyHealthIndicator myHealthIndicator() {
//        return new MyHealthIndicator();
//    }
//
//    @Bean
//    public MyHealthCheckHandler myHealthCheckHandler(MyHealthIndicator myHealthIndicator) {
//        return new MyHealthCheckHandler(myHealthIndicator);
//    }

    public static void main(String[] args) {
        SpringApplication.run(TimeServiceApplication.class, args);
    }
}