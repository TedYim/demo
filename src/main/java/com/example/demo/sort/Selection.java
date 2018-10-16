package com.example.demo.sort;

/**
 * 选择排序
 */
public class Selection {

    public static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int tmp = arr[i];
            for (int j = i + 1; j < arr.length; j++) {
                int tmp1 = arr[j];
                if (tmp1 < tmp) {
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
        Selection.selectSort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }
}
