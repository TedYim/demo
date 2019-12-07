package com.example.demo.asyncread;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.FutureTask;

public abstract class AbstractQueryTask<T> implements Runnable {

    private Vector<T> vector = new Vector<T>();
    int pageSize;

    public AbstractQueryTask(int pageSize) {
        this.pageSize = pageSize;
    }

    boolean finish = false;

    /***
     * 获取总页数
     *
     * @return
     */
    public abstract int getTotal();


    /***
     * 是否结束
     * @return
     */
    public boolean isFinish() {
        return finish;
    }

    abstract FutureTask<List<T>> doRequestTask(int pageSize, int currentSize);

    public int getTotalPage(int total, int pageSize) {
        int totalPage = 0;
        int tempTotalPage = total / pageSize;

        totalPage = total % pageSize == 0 ? tempTotalPage : tempTotalPage + 1;

        return totalPage;
    }

    @Override
    public void run() {
        doTask(pageSize);
        ;
    }


    private void doTask(int pageSize) {
        int total = getTotal();
        int totalPage = getTotalPage(total, pageSize);

        try {
            for (int i = 0; i < totalPage; ) {

                FutureTask<List<T>> futureTask = null;
                FutureTask<List<T>> futureTask2 = null;

                futureTask = doRequestTask(pageSize, i);
                i = i + 1;
                if (i < totalPage) {
                    i = i + 1;
                    futureTask2 = doRequestTask(pageSize, i);
                }
                if (futureTask != null) {
                    vector.addAll(futureTask.get());
                }
                if (futureTask2 != null) {
                    vector.addAll(futureTask2.get());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish = true;
    }


    public T next() {
        if (!isFinish() && vector.size() > 0) {
            return vector.remove(0);
        }
        return null;
    }

    public void addDataList(List<T> dataList) {
        vector.addAll(dataList);
    }
}
