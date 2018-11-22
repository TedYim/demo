package com.example.demo.redisson.queue;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Lazy(false)
@Component
public class RedissionConsumer implements Runnable {


    private RedissonClient redisson;

    @Autowired
    public RedissionConsumer(RedissonClient redisson) {
        this.redisson = redisson;
        new Thread(this).start();
    }

    /**
     * poll, pollFromAny, pollLastAndOfferFirstTo和take方法内部采用话题订阅发布实现，在Redis节点故障转移（主从切换）或断线重连以后，内置的相关话题监听器将自动完成话题的重新订阅。
     */
    @Override
    public void run() {
        log.warn("--------------开始在Redission中消费数据--------");
        List<Message> msgs = Lists.newArrayListWithExpectedSize(3);
        RBlockingQueue<Message> queue = redisson.getBlockingQueue("msgQueue");
        while (true) {
            for (; ; ) {
                Message msg = queue.poll();
                if (msg != null) {
                    log.warn("消费数据到的数据为 : " + msg);
                    msgs.add(msg);
                    if (msgs.size() == 3) {
                        break;
                    }
                } else {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }

            //处理业务
            for (Message message : msgs) {
                try {
                    doWork(message);
                } catch (Exception e) {
                    queue.offer(message);
                    e.printStackTrace();
                }
            }


        }


        //Message obj = queue.peek();
        //SomeObject ob = queue.poll(10, TimeUnit.MINUTES);
    }

    private void doWork(Message message) {
        if (message.getId().equals(2L)) {
            throw new RuntimeException("2号消息出错了");
        }
    }
}
