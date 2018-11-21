package com.example.demo.spring_event.code;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by Ted on 2018/11/21.
 */
@Component
public class TestListenerCode implements ApplicationListener<EventDemo> {

    @Override
    public void onApplicationEvent(EventDemo event) {
        TestParam param = (TestParam) event.getSource();
        System.out.println(".......开始.......");
        System.out.println("发送邮件:" + param.getEmail());
        System.out.println(".......结束.....");
    }
}
