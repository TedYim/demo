package com.demo.health;

import com.demo.controller.HealthCheckController;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

/**
 * 自定义健康指示器(Spring Boot的内容)
 */
public class MyHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        if (HealthCheckController.isCanLinkDb) {
            return new Health.Builder(Status.UP).build();
        } else {
            return new Health.Builder(Status.DOWN).build();
        }
    }

}
