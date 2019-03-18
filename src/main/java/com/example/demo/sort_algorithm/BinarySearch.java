package com.example.demo.sort_algorithm;


public class BinarySearch {

    public static void main(String[] args) {
        int[] arr = {10, 12, 13, 14, 15, 16, 17, 18, 19};
        int i = myBinarySearch(arr, 11);
        System.out.println(i);
    }

    private static int myBinarySearch(int[] arr, int i) {
        int low = 0;
        int high = arr.length - 1;
        while (low < high) {
            int mid = low / 2 + high / 2;
            if (arr[mid] > i) {
                high = mid / 2;
            } else if (arr[mid] < i) {
                low = mid / 2;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
