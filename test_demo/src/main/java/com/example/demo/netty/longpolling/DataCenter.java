package com.example.demo.netty.longpolling;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 为了模拟服务端没有数据，需要等待的情况，这里使用BlockingQueue来模拟，
 * 不定期的往队列里面插入数据，同时对外提供获取数据的方法，使用的是take方法，没有数据会阻塞知道有数据为止；
 */
public class DataCenter {

    private static Random                random = new Random();
    private static BlockingQueue<String> queue  = new LinkedBlockingQueue<>();
    private static AtomicInteger         num    = new AtomicInteger();

    public static void start() {
        while (true) {
            try {
                Thread.sleep(1100000000);
                String data = "hello world" + num.incrementAndGet();
                queue.put(data);
                System.out.println("store data:" + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getData() throws InterruptedException {
        return queue.take();
    }

}

