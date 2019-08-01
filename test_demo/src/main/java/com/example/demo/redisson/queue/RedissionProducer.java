package com.example.demo.redisson.queue;


import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Lazy(false)
@Component
public class RedissionProducer implements Runnable {

    private RedissonClient redisson;

    @Autowired
    public RedissionProducer(RedissonClient redisson) {
        this.redisson = redisson;
        new Thread(this).start();
    }


    @Override
    public void run() {
        log.warn("开始往Redission中生产数据");
        RBlockingQueue<Message> queue = redisson.getBlockingQueue("msgQueue");
        for (long i = 0; i < 10; i++) {
            queue.offer(new Message(i, "这条消息" + i));
        }
    }
}
