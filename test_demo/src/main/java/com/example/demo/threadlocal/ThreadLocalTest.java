package com.example.demo.threadlocal;

import lombok.Data;
import scala.annotation.meta.field;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


public class ThreadLocalTest {

    public static void main(String[] args) throws Exception {

        Class<?> threadClass = Class.forName("java.lang.Thread");
        Constructor<?> constructor = threadClass.getConstructor(Runnable.class);
        Thread newThread = (Thread) constructor.newInstance(new Runnable() {
            @Override
            public void run() {
                ThreadLocalDemo demo1 = new ThreadLocalDemo();
                demo1.getThreadLocal1().set("Test");
                demo1.getThreadLocal2().set("Test1");

                ThreadLocalDemo demo2 = new ThreadLocalDemo();
                demo2.getThreadLocal1().set("Test");
                demo2.getThreadLocal2().set("Test1");
                try {
                    Thread.currentThread().join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        newThread.start();
        Field threadLocals = threadClass.getDeclaredField("threadLocals");
        threadLocals.setAccessible(true);
        Object o = threadLocals.get(newThread);
        System.out.println(o);

        //Thread.currentThread().threadLocals

        //System.out.println(Thread.currentThread().getId() + " ---- " + ThreadLocalDemo.threadLocal1.get());


        //System.out.println(Thread.currentThread().getId() + " ---- " + ThreadLocalDemo.threadLocal2.get());
        //Thread.currentThread().join();
    }

    @Data
    static class ThreadLocalDemo {
        public ThreadLocal<String> threadLocal1 = new ThreadLocal<>() ;
        public ThreadLocal<String> threadLocal2 = new ThreadLocal<>();


    }
}
