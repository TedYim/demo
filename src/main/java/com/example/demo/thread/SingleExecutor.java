package com.example.demo.thread;

import java.util.concurrent.*;

public class SingleExecutor {

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            System.out.println("111-start");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("111-end");
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("222-start");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("222-end");
        });

        Thread thread3 = new Thread(() -> {
            System.out.println("333-start");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("333-end");
        });

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        executorService.execute(thread1);
        executorService.execute(thread2);
        executorService.execute(thread3);

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ok");
        while (true) {
            System.out.println("线程池的状态TaskCount为 : " + threadPoolExecutor.getTaskCount());//3
            System.out.println("线程池的状态ActiveCount为 : " + threadPoolExecutor.getActiveCount());//一直是1
            System.out.println("线程池的状态PoolSize为 : " + threadPoolExecutor.getPoolSize());
            System.out.println("线程池的状态CorePoolSize为 : " + threadPoolExecutor.getCorePoolSize());
            System.out.println("线程池的状态LargestPoolSize为 : " + threadPoolExecutor.getLargestPoolSize());
            System.out.println("线程池的状态MaximumPoolSize为 : " + threadPoolExecutor.getMaximumPoolSize());
            System.out.println("线程池的状态Queue为 : " + threadPoolExecutor.getQueue());
            if (threadPoolExecutor.getCompletedTaskCount() >= 3) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
