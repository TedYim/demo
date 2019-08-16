package com.example.demo.thread;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Lazy(false)
@Component
public class SingleThread implements Runnable {

    // 任务调度
    private ExecutorService executorService;

    public SingleThread() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        this.startProducer();
    }

    private void startProducer() {
        executorService = Executors.newSingleThreadExecutor(Threads.buildJobFactory("Job-Producer-Single-Thread" + "-%d"));
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("---------------生产者线程还在运行----------------" + i);
                    if (i == 30) throw new RuntimeException("模拟一个异常");


                }
            }// --> end thread run
        });// --> end execute
    }


    static class Threads {

        /**
         * 创建ThreadFactory，使得创建的线程有自己的名字而不是默认的"pool-x-thread-y"，
         * 在用threaddump查看线程时特别有用。 格式如"mythread-%d"，使用了Guava的工具类
         */
        public static ThreadFactory buildJobFactory(final String nameFormat) {
            final ThreadFactory threadFactory = Executors.defaultThreadFactory();
            return new ThreadFactory() {

                // 记录线程编号
                AtomicInteger count = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = threadFactory.newThread(r);
                    thread.setName(String.format(nameFormat, count.getAndIncrement()));
                    return thread;
                }
            };
        }

    }
}