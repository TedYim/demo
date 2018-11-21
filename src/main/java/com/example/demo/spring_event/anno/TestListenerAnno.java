package com.example.demo.spring_event.anno;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Ted on 2018/11/21.
 */
@Component
public class TestListenerAnno {

    /**
     * 异步执行
     * @param event
     */
    @Async
    @EventListener
    public void listenUserRegisterEvent(EventDemo event) {
        TestParam param = (TestParam) event.getSource();
        System.out.println(".......开始.......");
        System.out.println("发送邮件:" + param.getEmail());
        System.out.println(".......结束.....");
    }
}
