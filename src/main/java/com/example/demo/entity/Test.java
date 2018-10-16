package com.example.demo.entity;


public class Test {

    public static int i = 1;

    static {
        System.out.println(" init...");
    }

    public Test() {
        System.out.println("信件一个类...");
    }

    private Integer id;
    private String  name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
