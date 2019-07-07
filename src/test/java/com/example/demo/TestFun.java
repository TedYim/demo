package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestFun {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RedisTemplate<String, Object> template;

    public void test() {
        RedisConnection connection = redisConnectionFactory.getConnection();
        template.opsForSet().add("set", "1");
        template.opsForSet().add("set", "2");
        template.opsForSet().add("set", "3");
        BoundSetOperations<String, Object> set = template.boundSetOps("set");
        System.out.println(set);
        template.delete("set");
        set = template.boundSetOps("set");
        System.out.println(set);
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {


            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName("Job-Consumer-1");
            return thread;
        });

        executorService.execute(() -> {

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    //在睡眠的时候调用了thread.interrupt,导致抛出了异常
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName() + " 在休眠中被关闭了!");
                    break;
                }
                System.out.println("-----------" + Thread.currentThread().getName());

            }
            System.out.println(Thread.currentThread().getName() + " 被正常关闭了!");
        });

        TimeUnit.SECONDS.sleep(300);

        executorService.shutdownNow();
        System.out.println(Thread.currentThread().getName() + " 此时关闭!");

    }

}
