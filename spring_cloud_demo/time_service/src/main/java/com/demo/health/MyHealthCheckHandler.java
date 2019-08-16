package com.demo.health;

import com.alibaba.fastjson.JSON;
import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Status;

/**
 * 健康检查处理器
 */
@Slf4j
public class MyHealthCheckHandler implements HealthCheckHandler {

    private MyHealthIndicator myHealthIndicator;

    public MyHealthCheckHandler(MyHealthIndicator myHealthIndicator) {
        this.myHealthIndicator = myHealthIndicator;
    }

    @Override
    public InstanceStatus getStatus(InstanceStatus instanceStatus) {
        log.info("instanceStatus is : {}", JSON.toJSONString(instanceStatus));
        Status status = myHealthIndicator.health().getStatus();
        if (status.equals(Status.UP)) {
            return InstanceStatus.UP;
        } else {
            return InstanceStatus.DOWN;
        }
    }

}
