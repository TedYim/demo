package com.example.demo.thread;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {
    //线程池的状态
    volatile int runState;//volatile变量是一个轻量级的synchronized。
    static final int RUNINNG    = 0;
    static final int SHUTDOWN   = 1;
    static final int STOP       = 2;
    static final int TERMINATED = 3;

    private int poolSize;
    private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();//任务队列，线程安全的
    private ReentrantLock                 lock  = new ReentrantLock();//事务锁，用作同步
    private HashSet<WorkThread>           work  = new HashSet<>();//存放工作线程

    public ThreadPool(int poolSize) {
        this.poolSize = poolSize;
        init();//初始化
        runState = RUNINNG;
    }

    //提交任务
    public boolean execute(Runnable task) {
        if (task == null)
            throw new NullPointerException();
        if (runState != RUNINNG) {
            return false;//线程池已经终止。
        }
        return queue.add(task);//private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();//任务队列，线程安全的
    }

    //初始化工作线程
    private void init() {
        for (int i = 0; i < poolSize; i++) {
            WorkThread w = new WorkThread();
            work.add(w);//private HashSet<WorkThread> work  = new HashSet<>();//存放工作线程
            w.start();//执行任务
        }
        log("线程池初始化完成");
    }

    //修改线程状态为SHUTDOWN,此时不能再添加新任务，但已添加的任务会执行完。
    public void shutdown() {
        runState = SHUTDOWN;
    }

    //修改线程状态为SHUTDOWN,此时不能再添加新任务，并尝试停止运行任务，队列中的任务可能不会全部运行完。
    public void shutdownNow() {
        runState = STOP;
    }

    //工作线程
    class WorkThread extends Thread {
        boolean runing = true;

        @Override
        public void run() {
            while (runing) {
                Runnable task = null;
                task = getTask();
                if (task != null) {
                    task.run();
                    log(this + "$" + task.toString() + "运行完成");
                } else if (task == null & runState >= SHUTDOWN) {
                    runing = false;
                    lock.lock();
                    work.remove(this);
                    if (work.size() == 0) {
                        runState = TERMINATED;
                        log("线程池停止");
                    }
                    lock.unlock();
                }
            }
        }

    }

    //到任务队列取一个任务
    private Runnable getTask() {
        if (runState > SHUTDOWN) {
            return null;
        } else {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
            }
        }
        return null;
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    //test
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(2);
        pool.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                log("正在运行A");
            }
        });
        pool.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                log("正在运行B");
            }
        });
        pool.execute(new Runnable() {

            @Override
            public void run() {
                log("正在运行C");
            }
        });
        pool.shutdown();
//      pool.shutdownNow();
    }
}
