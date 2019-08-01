package com.example.demo;

@FunctionalInterface
public interface ITest {

    void do1();

    default void do2() {
        System.out.println("do2");
    }

    static void do3() {
        System.out.println("do3");
    }
}
