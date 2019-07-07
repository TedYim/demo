package com.example.demo.sort;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ClassTest {

    public static void main(String[] args) {

        List<String> l = new ArrayList(Arrays.asList("one", "two"));
        Stream<String> sl = l.stream();
        Stream<String> parallel = sl.parallel();
        Stream<String> sequential = parallel.sequential();
        l.add("three");
        sequential.forEach(System.out::println);

    }
}
