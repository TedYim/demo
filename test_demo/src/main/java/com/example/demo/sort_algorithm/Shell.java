package com.example.demo.sort_algorithm;

/**
 * 希尔排序
 */
public class Shell {

    public static void sort(int[] intArrays) {
        int length = intArrays.length;
        int h = 1;
        int block = 3;//分块大小（大于1的值）
        //h为分区后每块有多少个元素
        while (h < length / block) {
            h = block * h + 1; //通过循环算出h的取值，当分区大小为3时，h序列为1, 4, 13, 40, ...
        }
        while (h >= 1) {
            int n, i, j, k;
            //分割后，产生n个子序列
            for (n = 0; n < h; n++) {
                //分别对每个子序列进行直接插入排序
                for (i = n + h; i < length; i += h) {
                    for (j = i - h; j >= 0 && intArrays[i] < intArrays[j]; j -= h) {

                    }
                    int tmp = intArrays[i];
                    for (k = i; k > j + h; k -= h) {
                        intArrays[k] = intArrays[k - h];
                    }
                    intArrays[j + h] = tmp;
                }
            }
            //直接插入排序完后，减少每块区里的元素。也就是说增大块区的数量，直到最后h=1(每块区里只有一个元素时，排序完成)
            h = h / block;
        }
    }


}
