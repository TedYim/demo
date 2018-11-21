package com.example.demo.redisson.queue;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Lazy(false)
@Component
public class RedissionConsumer implements Runnable {

    @Autowired
    private RedissonClient redisson;


    public RedissionConsumer() {
        new Thread(this).start();
    }

    /**
     * poll, pollFromAny, pollLastAndOfferFirstTo和take方法内部采用话题订阅发布实现，在Redis节点故障转移（主从切换）或断线重连以后，内置的相关话题监听器将自动完成话题的重新订阅。
     */
    @Override
    public void run() {
        log.warn("--------------开始在Redission中消费数据--------");
        RBlockingQueue<Message> queue = redisson.getBlockingQueue("msgQueue");
        while (true) {
            Message someObj = queue.poll();
            log.warn("消费数据到的数据为 : " + someObj);
        }


        //Message obj = queue.peek();
        //SomeObject ob = queue.poll(10, TimeUnit.MINUTES);
    }
}
