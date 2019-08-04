package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringCloudApplication
public class TimeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeServiceApplication.class, args);
    }
}