package com.example.demo.sort;


/**
 * 直接插入排序
 */
public class Insertion {
    //BinaryInsertion
    /**
     * 直接插入排序算法方法
     */
    public static void sort(int intArrays[]) {
        int length = intArrays.length;
        int i, j;
        for (i = 1; i < length; i++) {
            for (j = i - 1; j >= 0 && intArrays[i] < intArrays[j]; j--) {
                //intArrays[i]代表要插入的数字，intArrays[j]代表需要比较大小的数字，j递减
                //当intArrays[i]大于intArrays[j]时（intArrays[i]插入的位置，也就是说插在j的后一位）或者intArrays[i]为当前数组的最小值时（此时的j为-1,也就是说intArrays[i]要插在第一位）返回j
            }
            //将intArrays[i]保存住，因为要j以后的数组向后移一位
            int temp = intArrays[i];
            for (int k = i; k > j + 1; k--) {
                //将i到j范围的数组向后移一位
                intArrays[k] = intArrays[k - 1];
            }
            //intArrays[i]插在j的后一位
            intArrays[j + 1] = temp;
        }
    }

    /**
     * 执行入口，intArrays：待排序的数组，displaySort：是否显示排序前和排序后的内容
     */
    public static void run(int intArrays[], boolean displaySort) {
        int arrays[] = intArrays.clone();
        if (displaySort) {
            NumberUtils.display(arrays, 1);
        }
        long startTime = System.currentTimeMillis();
        sort(arrays);
        long endTime = System.currentTimeMillis();
        if (displaySort) {
            NumberUtils.display(arrays, 2);
        }
        System.out.println("插入排序用时：" + (endTime - startTime) + "毫秒");
    }

    /**
     * 测试排序用的主方法
     */
    public static void main(String[] args) {
        //数组长度
        int length = 30000;
        //最大值
        int max = 100000000;
        //是否打印排序后的内容
        boolean display = true;
        //随机获取的排序数组
        int intArrays[] = NumberUtils.getRandomArs(length, max);
        //插入排序
        Insertion.run(intArrays, display);
    }
}

