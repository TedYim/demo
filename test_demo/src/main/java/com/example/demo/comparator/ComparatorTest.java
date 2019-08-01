package com.example.demo.comparator;

import java.util.*;
import java.util.stream.Collectors;

public class ComparatorTest {


    public static void main(String[] args) {

        Employee e1 = new Employee("John", 25, 3000, 9922001);
        Employee e2 = new Employee("Ace", 22, 2000, 5924001);
        Employee e3 = new Employee("Keith", 35, 4000, 3924401);

        List<Employee> employees = new ArrayList<>();
        employees.add(e1);
        employees.add(e2);
        employees.add(e3);

        /**
         *     @SuppressWarnings({"unchecked", "rawtypes"})
         *     default void sort(Comparator<? super E> c) {
         *         Object[] a = this.toArray();
         *         Arrays.sort(a, (Comparator) c);
         *         ListIterator<E> i = this.listIterator();
         *         for (Object e : a) {
         *             i.next();
         *             i.set((E) e);
         *         }
         *     }
         *
         *     sort 对象接收一个 Comparator 函数式接口，可以传入一个lambda表达式
         */
        employees.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));

        Collections.sort(employees, (o1, o2) -> o1.getName().compareTo(o2.getName()));

        employees.forEach(System.out::println);

        /**
         * Comparator.comparing 方法的使用
         *
         * comparing 方法接收一个 Function 函数式接口 ，通过一个 lambda 表达式传入
         *
         */
        employees.sort(Comparator.comparing(e -> e.getName()));

        /**
         * 该方法引用 Employee::getName 可以代替 lambda表达式
         */
        employees.sort(Comparator.comparing(Employee::getName));

        Collections.sort(employees, Comparator.comparing(
                Employee::getName, (s1, s2) -> {
                    return s2.compareTo(s1);
                }));

        /**
         *  相反的排序规则
         */
        Collections.sort(employees, Comparator.comparing(Employee::getName).reversed());

        employees.forEach(System.out::println);

        employees.add(null);  //插入一个null元素
        Collections.sort(employees, Comparator.nullsFirst(Comparator.comparing(Employee::getName)));
        employees.forEach(System.out::println);


        Collections.sort(employees, Comparator.nullsLast(Comparator.comparing(Employee::getName)));
        employees.forEach(System.out::println);

        Collections.sort(employees, Comparator.comparing(Employee::getAge).thenComparing(Employee::getName));
        employees.forEach(System.out::println);

    }
}


/**
 * [Employee(name=John, age=25, salary=3000.0, mobile=9922001),
 * Employee(name=Ace, age=22, salary=2000.0, mobile=5924001),
 * Employee(name=Keith, age=35, salary=4000.0, mobile=3924401)]
 */
class Employee {
    String name;
    int    age;
    double salary;
    long   mobile;

    // constructors, getters & setters


    public Employee(String name, int age, double salary, long mobile) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append(", salary=").append(salary);
        sb.append(", mobile=").append(mobile);
        sb.append('}');
        return sb.toString();
    }
}
