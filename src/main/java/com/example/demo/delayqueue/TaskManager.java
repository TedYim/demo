package com.example.demo.delayqueue;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;

@Data
public class TaskManager implements ApplicationContextAware, InitializingBean, DisposableBean {

    private static final int threadCount = 5;

    private DelayQueue<TaskMessage> queue = new DelayQueue<>();

    private List<TaskConsumer> taskConsumerList = new ArrayList<>();

    private List<Thread> threadList = new ArrayList<>();



    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i = 0; i < threadCount; i++) {
            TaskConsumer taskConsumer = new TaskConsumer(queue, i);
            taskConsumerList.add(taskConsumer);
            Thread thread = new Thread(taskConsumer);
            threadList.add(thread);
            thread.start();
        }
    }

    @Override
    public void destroy() throws Exception {
        for (int i = 0; i < threadList.size(); i++) {
            threadList.get(i).interrupt();
            taskConsumerList.get(i).setSignal(Boolean.FALSE);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
