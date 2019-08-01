package com.example.demo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.ClassLoader.getSystemClassLoader;

public class TestStreamAPI {

    @Test
    public void test() {
        List<String> list = Arrays.asList("abc", "efg", "xyz");
        list.stream()//
                .flatMap(TestStreamAPI::string2Stream)//
                .forEach(System.out::println);
    }

    /**
     * 接收一个字符串将其所以的字符添加到list中然后返回stream
     * @param str
     * @return
     */
    public static Stream<Character> string2Stream(String str) {
        List<Character> list = new ArrayList<>();
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            list.add(c);
        }
        return list.stream();
    }

    @Test
    public void test1() {
        List<String> list = Arrays.asList("d", "a", "c");
        list.stream()//
                .sorted((x,y) -> -x.compareTo(y))//
                .forEach(System.out::println);
    }

    @Test
    public void test2() {
        List<Integer> list = Arrays.asList(10, 5, 7, 3);
        Optional<Integer> first = list.stream()//
                .findAny();
        Integer val = first.get();
        System.out.println(val);//输出10
    }

    @Test
    public void test3() throws Exception {
        System.out.println(getSystemClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());
    }
}
