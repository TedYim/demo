package com.example.demo;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Calendar;

public class SimpleTest {

    public static String test = "";

    public static void main(String[] args) throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTimeInMillis());
        calendar.setTimeInMillis(1542827305583L);
        System.out.println(calendar.getTime());

        int i1 = 10;
        int i2 = 10;
        System.out.println(i1 ^ i2);


        //创建一个引用队列
        ReferenceQueue queue = new ReferenceQueue();

        // 创建弱引用，此时状态为Active，并且Reference.pending为空，当前Reference.queue = 上面创建的queue，并且next=null
        WeakReference reference = new WeakReference(new Object(), queue);
        System.out.println(reference);
        // 当GC执行后，由于是弱引用，所以回收该object对象，并且置于pending上，此时reference的状态为PENDING
        System.gc();

        /* ReferenceHandler从pending中取下该元素，并且将该元素放入到queue中，此时Reference状态为ENQUEUED，Reference.queue = ReferenceENQUEUED */

        /* 当从queue里面取出该元素，则变为INACTIVE，Reference.queue = Reference.NULL */
        Reference reference1 = queue.remove();
        System.out.println(reference1);

    }
}
