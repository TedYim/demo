package com.example.demo.thread;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 增加守护线程来维护主线程的运行
 */
@Component
@Lazy
public class ThreadDemo implements Runnable {

    volatile Map<String, Thread>   threadMap;
    volatile Map<String, Runnable> runMap;

    public ThreadDemo() {
        System.err.println("ThreadDemo 初始化...");
        this.threadMap = new HashMap<>();
        this.runMap = new HashMap<>();
        Thread main = new Thread(this, "主线程");
        main.start();
        threadMap.put("main", main);
        new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getId() + " 守护线程在运行!!!!");
                Thread currentMain = threadMap.get("main");
                //监控t1
                if (currentMain.isAlive()) {
                    System.out.println(currentMain.getId() + " - " + currentMain.getName() + " 还活着......");
                } else {
                    System.err.println(currentMain.getId() + " - " + currentMain.getName() + " 挂了 , 正在重启......");
                    Thread newMain = new Thread(this, "主线程");
                    newMain.start();
                    threadMap.put("main", newMain);
                    System.err.println(currentMain.getId() + " - " + currentMain.getName() + " 重启成功!");
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void run() {
        int i = 1;
        while (true) {
            Thread currentMain = threadMap.get("main");
            System.out.println(currentMain.getId() + " - " + currentMain.getName() + " 正在执行!!");
            System.out.println(currentMain.getId() + " - " + currentMain.getName() + "正在工作 , 当期 i= " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i % 14 == 0) {
                Thread.currentThread().stop();//模拟主线程挂掉
            }
            i++;
        }
    }
}
