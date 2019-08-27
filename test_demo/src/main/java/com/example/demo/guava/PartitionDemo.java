package com.example.demo.guava;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class PartitionDemo {

    public static void main(String[] args) {
        List<String> ls = Arrays.asList("1,2,3,4,5,6,7,8,9,1,2,4,5,6,7,7,6,6,6,6,6,66".split(","));
        System.out.println(Lists.partition(ls, 5));
    }
}
