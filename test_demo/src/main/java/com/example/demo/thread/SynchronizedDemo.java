package com.example.demo.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedDemo {

    private final Object o1 = new Object();
    private final Object o2 = new Object();

    final String o3 = "asd";

    public void doWork() {

        synchronized (this) {
            System.out.println("111111111111111");
            try {
                o1.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("222222222222222");
        }

    }

    void LockDemo() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        lock.lock();
        condition.await();
    }

    void finalDemo(){
        final String a ;
        a = "asd";
        //a = "icj";//final变量不能赋值两次
        System.out.println(a);
    }

    public static void main(String[] args) {
        SynchronizedDemo demo = new SynchronizedDemo();
        //demo.doWork();

    }
}
