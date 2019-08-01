package com.example.demo.delayqueue;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;

@Slf4j
@Data
public class TaskConsumer implements Runnable {

    private DelayQueue<TaskMessage> queue;
    
    private Boolean signal;

    private Integer no;

    public TaskConsumer(DelayQueue<TaskMessage> queue, Integer no) {
        this.queue = queue;
        this.no = no;
    }

    @Override
    public void run() {
        while (signal) {
            try {
                TaskMessage take = queue.take();
                if (log.isInfoEnabled()) {
                    //log.info("处理线程的id为" + Thread.currentThread().getId() + ",消费消息内容为：" + take.getBody() + ",预计执行时间为" +
                  //          DateFormatUtils.timeStampToString(take.getDelay(TimeUnit.MILLISECONDS) + System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                }
                take.getConsumer().accept(take.getBody());
            } catch (InterruptedException e) {
                if (log.isInfoEnabled()) {
                    //log.info("id为" + threadId + "的处理线程被强制中断");
                }
            } catch (Exception e) {
                log.error("taskConsumer error", e);
            }
        }
        if (log.isInfoEnabled()) {
            //log.info("id为" + threadId + "的处理线程已停止");
        }
    }
}
