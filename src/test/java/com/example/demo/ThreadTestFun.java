package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThreadTestFun {


    @Test
    public void threadTest() throws Exception {

        Thread t1 = new Thread(() -> {
            int i = 0;
            while (true) {
                System.out.println("主线程正在执行!!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i % 7 == 0) {
                    Thread.currentThread().stop();
                }
                i++;
            }

        }, "执行线程");
        t1.start();
        Thread.sleep(1000);
        Thread t2 = new Thread(() -> {
            while (true) {
                //监控t1
                if (t1.isAlive()) {
                    System.out.println(t1.getName() + " 线程还活着......");
                } else {
                    System.err.println(t1.getName() + " 线程挂了 , 正在重启......");
                    t1.start();
                    System.err.println(t1.getName() + " 线程重启成功!");
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }, "守护线程");
        t2.start();

        Thread.sleep(Integer.MAX_VALUE);


    }


}
