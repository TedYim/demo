package com.example.demo.thread;

/**
 * Created by Ted on 2018/9/11.
 */
public class ThreadBlockedQ {
    public static void main(String[] args) throws InterruptedException {
        ThreadExcutor excutor = new ThreadExcutor(3);
        for (int i = 0; i < 10; i++) {
            excutor.exec(new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程 " + Thread.currentThread().getName() + " 在帮我干活");
                }
            });
        }
        excutor.shutdown();
    }
}
