package com.example.demo;

class Person {
    private String name;
    private int    age;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public void run() {
    }
}


interface IStudyable {
    public int study(int a, int b);
}

//public类，与java文件同名
public class Student extends Person implements IStudyable {
    private static int cnt = 5;

    static {
        cnt++;
    }

    private String sid;

    public Student(int age, String name, String sid) {
        super(age, name);
        this.sid = sid;
    }

    public void run() {
        System.out.println("run()...");
    }

    public int study(int a, int b) {
        int c = 10;
        int d = 20;
        return a + b * c - d;
    }

    public static int getCnt() {
        return cnt;
    }

    public static void main(String[] args) {

        String s = new String("a");
        String s3 = "a";//在s还没有intern之前先把引用放入String池
        String s2 = new String("a");
        s.intern();
        System.out.println(s2 == s2.intern());//预期false 实际fasle
        System.out.println(s == s2.intern());//预期true 实际fasle
        System.out.println(s.intern() == s2.intern());//预期true 实际true
        System.out.println(s.intern() == s);//false
        System.out.println(s.intern() == s2);//false

        System.out.println(s.intern() == s3);//true
        System.out.println("----------------------------");

        String s1 = new String("he") + new String("llo");
        String s1Intern = s1.intern();
        String s4 = "hello";
        System.out.println(s4 == s1Intern.intern());//true
        System.out.println(s1 == s1.intern());//false
        System.out.println(s1.intern() == s1);//false
        System.out.println(s4 == s1);//false
        System.out.println(s4 == s1.intern());//true

    }
}

class NewTest1 {
    public static String s1 = "static";  // 第一句

    public static void main(String[] args) {
        String s1 = new String("he") + new String("llo"); //第二句
        s1.intern();   // 第三句
        String s2 = "hello";  //第四句
        System.out.println(s1 == s2);//第五句，输出是true。
    }
}

class NewTest2 {
    public static void main(String[] args) {
        String s1 = new String("he") + new String("llo"); // ①
        String s2 = new String("h") + new String("ello"); // ②
        String s3 = s1.intern(); // ③
        String s4 = s2.intern(); // ④
        System.out.println(s1 == s3);
        System.out.println(s1 == s4);
    }
}
class NewTest3 {
    public static void main(String[] args) {
        String s1 = new String("he") + new String("llo"); // ①
        String s2 = s1.intern(); // ③
        String s3 = "hello";
        System.out.println(s1 == s3);
        System.out.println(s1 == s2);
        System.out.println(s3 == s2);
    }
}
