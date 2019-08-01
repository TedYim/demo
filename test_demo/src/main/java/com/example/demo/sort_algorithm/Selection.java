package com.example.demo.sort_algorithm;

/**
 * 选择排序
 */
public class Selection {

    public static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int tmp = arr[i];//待选择的元素
            for (int j = i + 1; j < arr.length; j++) {
                int tmp1 = arr[j];//待比较的元素
                if (tmp1 < tmp) { //如果
                    tmp = tmp1;
                    exchange(arr, i, j);
                }
            }
        }
    }

    private static void exchange(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        int[] arr = {3, 54, 6, 4, 43, 5, 6, 6, 6, 5, 5, 9};
        sort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    public static void sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int tmp = i;//暂时的最小值的坐标
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[tmp] > arr[j]) {
                    exchange(arr, tmp, j);//交换一次 , i位置的元素就是当前比较的最小值 , 当全部交换完之后 , 就是最小的元素
                }

            }
        }
    }
}
