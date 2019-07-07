package com.example.demo.netty.ch10;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.InternalThreadLocalMap;
import org.junit.Test;

public class FastThreadLocalTest {
/*    private static FastThreadLocal<Object> threadLocal0 = new FastThreadLocal<Object>() {
        @Override
        protected Object initialValue() {
            return new Object();
        }

        @Override
        protected void onRemoval(Object value) throws Exception {
            System.out.println("onRemoval");
        }
    };

    private static FastThreadLocal<Object> threadLocal1 = new FastThreadLocal<Object>() {
        @Override
        protected Object initialValue() {
            return new Object();
        }
    };


    public static void main(String[] args) {
        new Thread(() -> {
            Object object = threadLocal0.get();
            // .... do with object
            System.out.println(object);
            threadLocal0.set(new Object());

//            while (true) {
//                threadLocal0.set(new Object());
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }).start();

        new Thread(() -> {
            Object object = threadLocal0.get();
            // ... do with object
            System.out.println(object);
            while (true) {
                System.out.println(threadLocal0.get() == object);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/


    @Test
    public void test() {
        FastThreadLocal<String> threadLocal0 = new FastThreadLocal() {
            @Override
            protected String initialValue() throws Exception {
                return "hello netty";
            }

            @Override
            protected void onRemoval(Object value) throws Exception {
                System.out.println("value has be removed");
            }
        };

        FastThreadLocal<String> threadLocal1 = new FastThreadLocal() {
            @Override
            protected String initialValue() throws Exception {
                return "hello netty";
            }

            @Override
            protected void onRemoval(Object value) throws Exception {
                System.out.println("value has be removed");
            }
        };

//        new DefaultThreadFactory("FastThread-Pool").newThread(() -> {
//                            System.out.println(threadLocal0.get());
//                        }
//                ).start();

        new FastThreadLocalThread(() -> {
            threadLocal0.set("hello FastThreadLocalThread0");
            System.out.println(threadLocal0.get());
            FastThreadLocalThread fastThreadLocalThread = (FastThreadLocalThread) Thread.currentThread();
            InternalThreadLocalMap internalThreadLocalMap = fastThreadLocalThread.threadLocalMap();
            System.out.println(internalThreadLocalMap);
        }
        ).start();


        new FastThreadLocalThread(() -> {
            threadLocal1.set("hello FastThreadLocalThread1");
            System.out.println(threadLocal1.get());
            FastThreadLocalThread fastThreadLocalThread = (FastThreadLocalThread) Thread.currentThread();
            InternalThreadLocalMap internalThreadLocalMap = fastThreadLocalThread.threadLocalMap();
            System.out.println(internalThreadLocalMap);
        }
        ).start();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
