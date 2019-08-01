package com.example.demo.comparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ted on 2019/4/27.
 */
public class Java8Test {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 4, 2, 6, 2, 8);
        list.sort(Comparator.naturalOrder());
        list.sort(Integer::compareTo);
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(list);
    }
}
