package com.example.demo.sort_algorithm;

import java.util.Random;

public class MergeSort {

    public static void mergeSort(int[] array, int low, int high) {
        int mid = low + (high - low) / 2;    // mid = (high + low)/2的另一种算法,避免溢出
        if (low < high) {
            mergeSort(array, low, mid);      // 对array[low…mid]归并排序
            mergeSort(array, mid + 1, high);    // 对array[mid+1…high]归并排序
            mergeArray(array, low, mid, high);// 融合
        }
    }

    public static void mergeArray(int[] array, int low, int mid, int high) {
        int[] temp = new int[mid - low + 1];      //创建临时数组,只需要创建前一半即可
        for (int i = 0, j = low; i < temp.length; i++, j++) {
            temp[i] = array[j];          //对临时数组进行赋值 , 赋值前半个数组
        }
        int i = 0, j = mid + 1, m = low;
        while (i < temp.length && j <= high) {     //将两个有序数组归并
            if (temp[i] <= array[j]) {      //小于等于是为了维持稳定性
                array[m] = temp[i];
                i++;
                m++;
            } else {
                array[m] = array[j];
                m++;
                j++;
            }
        }
        while (i < temp.length)              //最后将剩余的元素填充
            array[m++] = temp[i++];

		/*while(j <= high)
            array[m++] = array[j++];
			这一步其实可以不用做,因为此时array[j]==array[m]
		*/

    }

    public static void main(String[] args) {
        Random random = new Random();
        int[] array = new int[50];
        System.out.println("We randomly create an array, the array is below: ");
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(500);        //生成500以内的随机数
            System.out.print(array[i] + " ");
        }
        System.out.println();
        System.out.println("After selection sort, the answer is: ");
        mergeSort(array, 0, array.length - 1);
        for (int i : array) {
            System.out.print(i + " ");
        }
        System.out.println();

    }

}
