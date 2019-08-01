package com.example.demo;

/**
 * Created by Ted on 2019/4/9.
 */
public class StringTest {

    public static void main(String[] args) throws Exception {
        Class<A> aClass = A.class;//A.class文件只会触发到加载阶段,不会触发链接
        //Class.forName("com.example.demo.A");//会触发整个A.class类加载
    }

    private final byte[] by = new byte[0];//0个元素的byte数组比一个Object对象还小

    public void doSome() {
        synchronized (by) {

        }
    }


}

class A{
    static {
        System.out.println("A static init ...");
    }

    public A() {
        System.out.println("A init ...");
    }
}
