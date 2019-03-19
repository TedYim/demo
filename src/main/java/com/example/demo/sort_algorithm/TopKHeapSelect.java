package com.example.demo.sort_algorithm;

import java.util.PriorityQueue;
import java.util.Queue;

public class TopKHeapSelect {


    /**
     * 创建k个节点的小根堆
     *
     * @param a
     * @param k
     * @return
     */
    private int[] createHeap(int a[], int k) {
        int[] result = new int[k];//创建一个k个元素的临时数组
        for (int i = 0; i < k; i++) {
            result[i] = a[i];//将整个数组的前三个元素赋值给临时数组
        }
        for (int i = 1; i < k; i++) {
            int child = i;
            int parent = (i - 1) / 2;//父节点坐标
            int temp = a[i];//儿子节点赋值给temp
            while (parent >= 0 && child != 0 && result[parent] > temp) {
                result[child] = result[parent];
                child = parent;
                parent = (parent - 1) / 2;
            }
            result[child] = temp;
        }
        return result;

    }

    private void insert(int a[], int value) {
        a[0] = value;
        int parent = 0;

        while (parent < a.length) {
            int lchild = 2 * parent + 1;
            int rchild = 2 * parent + 2;
            int minIndex = parent;
            if (lchild < a.length && a[parent] > a[lchild]) {
                minIndex = lchild;
            }
            if (rchild < a.length && a[minIndex] > a[rchild]) {
                minIndex = rchild;
            }
            if (minIndex == parent) {
                break;
            } else {
                int temp = a[parent];
                a[parent] = a[minIndex];
                a[minIndex] = temp;
                parent = minIndex;
            }
        }

    }

    private int[] getTopKByHeap(int input[], int k) {
        int heap[] = this.createHeap(input, k);
        for (int i = k; i < input.length; i++) {
            if (input[i] > heap[0]) {
                this.insert(heap, input[i]);
            }


        }
        return heap;

    }

    public static void main(String[] args) {
        int a[] = {4, 3, 5, 1, 2, 8, 9, 10};
        int result[] = new TopKHeapSelect().getTopKByHeap(a, 3);
        for (int temp : result) {
            System.out.println(temp);
        }
        int i = new TopKHeapSelect().findKthLargestByPriorityQueue(a, 3);
        System.out.println("=======  " + i);

        //如果要实现自定义优先级队列,要重写Comparator
    }


    /**
     * 通过优先队列实现最大的TopK
     * 实质上就是通过最小根堆来实现
     *
     * @param num
     * @param k
     * @return
     */

    public int findKthLargestByPriorityQueue(int[] num, int k) {
        if (num.length < k) return 0;
        Queue<Integer> minQueue = new PriorityQueue<>(k + 1);
        for (int n : num) {
            //当优先队列没有装满或者n大于优先队列的队头时,将n插入队列
            if (minQueue.size() < k || n > minQueue.peek()) minQueue.offer(n);
            if (minQueue.size() > k) minQueue.poll();
        }
        return minQueue.peek();
    }
}
