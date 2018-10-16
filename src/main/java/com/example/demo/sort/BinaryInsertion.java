package com.example.demo.sort;

/**
 * 二分法直接插入
 */
public class BinaryInsertion {

    public static void sort(int intArrays[]) {


        for (int i = 1; i < intArrays.length; i++) {
            int sortEle = intArrays[i];//待排序元素
            int midIndex = i / 2;//查找之前信息的中点下标
            int midEle = intArrays[midIndex];//中点元素
            while (sortEle > midEle) {
                //在左边
                midIndex = midIndex / 2;
                midEle = intArrays[midIndex];
            }

            while (sortEle < midEle) {
                //在右边
                midIndex = (midIndex + i) / 2;
                midEle = intArrays[midIndex];
            }
            //移位
            for (int j = i; j > midIndex + 1; j--) {
                intArrays[i] = intArrays[i - 1];
            }
            intArrays[midIndex] = sortEle;

        }
    }

    public static void main(String[] args) {
        int[] arr = {7, 6, 9, 4, 7, 9, 0, 0, 8, 6, 7};
        BinaryInsertion.sort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(" " + arr[i]);
        }
    }
}
