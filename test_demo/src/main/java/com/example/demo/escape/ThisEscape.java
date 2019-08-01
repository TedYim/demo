package com.example.demo.escape;

import java.util.concurrent.TimeUnit;

public class ThisEscape {

    public int i = 0;

    public ThisEscape() {
        new Thread(new EscapeRunnable()).start();
        // ...其他代码
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i = 1;
    }

    private class EscapeRunnable implements Runnable {
        @Override
        public void run() {
            // 在这里通过ThisEscape.this就可以引用外围类对象, 但是此时外围类对象可能还没有构造完成, 即发生了外围类的this引用的逃逸
            System.out.println(ThisEscape.this.i + "=========11111");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(ThisEscape.this.i + "=========22222");
        }
    }

    public static void main(String[] args) {
        ThisEscape thisEscape = new ThisEscape();
    }
}
