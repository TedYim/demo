package com.example.demo;

import java.util.Calendar;

public class SimpleTest {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTimeInMillis());
        calendar.setTimeInMillis(1542827305583L);
        System.out.println(calendar.getTime());

    }
}
