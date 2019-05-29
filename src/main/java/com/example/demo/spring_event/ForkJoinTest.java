package com.example.demo.spring_event;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest extends RecursiveTask<Long> {
    /**
     *
     */
    private static final long serialVersionUID = -563064712545564927L;
    /**
     * 分派新任务的界限值
     */
    private final        int  limit            = 10000;
    private long start;
    private long end;
    private static int count = 1;

    public ForkJoinTest(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long result = 0;
        if (end - start + 1 < limit) {  //如果任务小于界限值，直接处理
            for (long i = start; i <= end; i++) result += i;
        } else {    //若大于界限值，就分叉任务
            long middle = (end + start) / 2;        //这里分叉为2个任务
            ForkJoinTest task1 = new ForkJoinTest(start, middle);
            ForkJoinTest task2 = new ForkJoinTest(middle + 1, end);
            System.err.println("计算次数:" + count++);
            //递归处理，子任务执行compute方法fork出孙任务，这里就像一颗二叉树一样
            invokeAll(task1, task2);
            long result1 = task1.join();
            long result2 = task2.join();
            result = result1 + result2;
        }
        return result;
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long start = System.nanoTime();
        //生成一个计算任务，负责计算1+……+10
        ForkJoinTest task = new ForkJoinTest(1, 100000);
        ForkJoinTask<Long> res = forkJoinPool.submit(task);
        long end = System.nanoTime();
        try {
            System.out.println("相加结果：" + res.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("耗时：" + (end - start) + "ns");
        int[][] a = new int[10][10];

    }

/*    public static String query(String q, List<String> engines) {
        Optional<String> result = engines.stream().parallel().map((base) -> {
            String url = base + q;
            return WS.url(url).get();
        }).findAny();
        return result.get();
    }*/
}
