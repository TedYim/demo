package com.example.demo;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueTest {

    @Test
    public void testQueuePeek() {
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        queue.offer(5);
        System.out.println(queue);
        System.out.println("-------------------------------------------------");
        Integer str = null;
        for (int i = queue.size(); i > 0; i--) {
            str = queue.poll();
            System.out.println(str);
        }

        while (queue.size() > 0) {  //size()方法，得队列中元素个数
            str = queue.poll();
            System.out.println(str);
        }
    }

    /**
     * 在遍历过程中，如果已经遍历的数组上的内容变化了，迭代器不会抛出ConcurrentModificationException异常。
     * 如果未遍历的数组上的内容发生了变化，则有可能反映到迭代过程中。
     * 这就是ConcurrentHashMap迭代器弱一致的表现。
     */
    @Test
    public void testConcurrentHashMap() {
        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put(1, 1);
        concurrentHashMap.put(2, 2);
        concurrentHashMap.put(3, 3);
        concurrentHashMap.put(4, 4);
        concurrentHashMap.put(5, 5);
        for (Integer integer : concurrentHashMap.keySet()) {
            System.out.println(integer);
            if (integer.equals(3))
                concurrentHashMap.remove(integer);
        }

    }

    /**
     * 会抛出 java.util.ConcurrentModificationException
     */
    @Test
    public void testHashMap() {
        Map<Integer, Integer> concurrentHashMap = new HashMap<>();
        concurrentHashMap.put(1, 1);
        concurrentHashMap.put(2, 2);
        concurrentHashMap.put(3, 3);
        concurrentHashMap.put(4, 4);
        concurrentHashMap.put(5, 5);
        for (Integer integer : concurrentHashMap.keySet()) {
            System.out.println(integer);
            if (integer.equals(3))
                concurrentHashMap.remove(integer);
        }

    }
}
