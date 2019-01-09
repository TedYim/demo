package com.example.demo.sort;

public class Insertion {

    public static int[] sort(int[] arr, int l, int r) {
        for (int i = l + 1; i < r; i++) {//i是要插入的数据
            for (int j = l; j < i; j++) {//j是之前已经排好序的数据
                if (arr[i] > arr[j]) {
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }

        }
        return arr;
    }


    public static void main(String[] args) {
        int[] ints = {5, 4, 3, 2, 3, 4, 6, 6};
        int[] sort = sort(ints, 0, ints.length);
        for (int i : sort) {
            System.out.print(i+" , ");
        }
    }
}
