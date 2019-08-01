package com.example.demo.sort_algorithm;

public class BubbleSort {

    public static void main(String[] args) {
        System.out.println("Hello World");
        int[] a = {12, 20, 5, 16, 15, 1, 30, 45, 23, 9};
        int start = 0;
        int end = a.length - 1;
        sort(a, start, end);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    private static void sort(int[] a, int start, int end) {
        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[i] > a[j]) {
                    swap(a, i, j);
                }
            }
        }

    }

    private static void swap(int[] a, int i, int j) {
        //System.out.println(a[i] + " : " + a[j]);
        a[i] = a[i] ^ a[j];
        a[j] = a[i] ^ a[j];
        a[i] = a[i] ^ a[j];
        //System.out.println(a[i] + " : " + a[j]);
    }
}
