package com.demo.config;

import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * client端是怎么获取server端的地址的呢？
 * 默认是从配置文件里面取得，如果需要更灵活的控制，
 * 可以通过override getEurekaServerServiceUrls方法来提供自己的实现。
 * 定期更新频率可以通过eureka.client.eurekaServiceUrlPollIntervalSeconds配置
 */
@Component
public class MyEurekaClientConfigBean extends EurekaClientConfigBean {

    @Override
    public List<String> getEurekaServerServiceUrls(String myZone) {

        try {
            //TODO 这里实现自定义获取EurekaServer地址的方法
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return super.getEurekaServerServiceUrls(myZone);
        }
    }
}
