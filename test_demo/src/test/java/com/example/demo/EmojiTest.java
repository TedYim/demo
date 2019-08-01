package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class EmojiTest {

    public static JSONObject emojiFilter(String obj) {
        byte[] conByte = obj.getBytes();
        for (int i = 0; i < conByte.length; i++) {
            if ((conByte[i] & 0xF8) == 0xF0) {
                System.err.println("此报文包含emoji表情!");
                for (int j = 0; j < 4; j++) {
                    conByte[i + j] = 0x7E;
                }
                i += 3;
            }
        }
        String content = new String(conByte).replaceAll("~~~~", "");
        return JSON.parseObject(content);
    }


    public static void clean(MappedByteBuffer mappedByteBuffer) {
        ByteBuffer buffer = mappedByteBuffer;
        if (buffer == null || !buffer.isDirect() || buffer.capacity() == 0)
            return;
        invoke(invoke(viewed(buffer), "cleaner"), "clean");
    }

    private static Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    Method method = method(target, methodName, args);
                    method.setAccessible(true);
                    return method.invoke(target);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }

    private static Method method(Object target, String methodName, Class<?>[] args)
            throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }

    private static ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("attachment")) {
                methodName = "attachment";
                break;
            }
        }
        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null)
            return buffer;
        else
            return viewed(viewedBuffer);
    }


    public static void main(String[] args) throws Exception {
        //JSONObject jsonObject = emojiFilter("{\"memberName\":\"小小郑\uD83D\uDE00\uD83D\uDE01\uD83D\uDE02\"}");
        //System.out.println(jsonObject);
        /*System.out.println(SimpleTest.test);
        SimpleTest.test = "123";
        System.out.println(SimpleTest.test);

        System.out.println(System.currentTimeMillis());

        ;
        System.out.println(new Date(1550459020495L));
        try {
            Class instance = Integer.class.getClass().newInstance();
            System.out.println(instance instanceof Class );
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

        /*File file = new File("D://words.txt");
        FileInputStream inputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[2048];
        while ((bufferedInputStream.read(buffer)) != -1) {
            System.out.println("=========" + new String(buffer) + "\n");
        }
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String s = bufferedReader.readLine();


        FileChannel fileChannel = new RandomAccessFile(new File("db.data"), "rw").getChannel();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());

        // 写
        byte[] data = new byte[4];
        int position = 8;
        //从当前 mmap 指针的位置写入 4b 的数据
        mappedByteBuffer.put(data);
        //指定 position 写入 4b 的数据
        MappedByteBuffer subBuffer = mappedByteBuffer.slice();
        subBuffer.position(position);
        subBuffer.put(data);

        // 读
        byte[] data = new byte[4];
        int position = 8;
        //从当前 mmap 指针的位置读取 4b 的数据
        mappedByteBuffer.get(data)；
        //指定 position 读取 4b 的数据
        MappedByteBuffer subBuffer = mappedByteBuffer.slice();
        subBuffer.position(position);
        subBuffer.get(data);

        ExecutorService executor = Executors.newFixedThreadPool(64);
        AtomicLong wrotePosition = new AtomicLong(0);
        for (int i = 0; i < 1024; i++) {
            final int index = i;
            executor.execute(() -> {
                fileChannel.write(ByteBuffer.wrap(new byte[4 * 1024]), wrotePosition.getAndAdd(4 * 1024));
            });
        }

        ExecutorService executor = Executors.newFixedThreadPool(64);
        AtomicLong wrotePosition = new AtomicLong(0);
        for (int i = 0; i < 1024; i++) {
            final int index = i;
            executor.execute(() -> {
                write(new byte[4 * 1024]);
            });
        }

        public synchronized void write ( byte[] data){
            fileChannel.write(ByteBuffer.wrap(new byte[4 * 1024]), wrotePosition.getAndAdd(4 * 1024));
        }


        File file = new File("C:\\mycode\\hello.txt");
        *//**
         * model各个参数详解
         * r 代表以只读方式打开指定文件
         * rw 以读写方式打开指定文件
         * rws 读写方式打开，并对内容或元数据都同步写入底层存储设备
         * rwd 读写方式打开，对文件内容的更新同步更新至底层存储设备
         *//*
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            //获取RandomAccessFile对象文件指针的位置，初始位置是0
            System.out.println("RandomAccessFile文件指针的初始位置:" + raf.getFilePointer());
            //移动文件指针位置
            raf.seek(0);
            byte[] buff = new byte[1024];
            //用于保存实际读取的字节数
            int hasRead = 0;
            //循环读取
            while ((hasRead = raf.read(buff)) > 0) {
                //打印读取的内容,并将字节转为字符串输入
                System.out.println(new String(buff, 0, hasRead));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
    }
}
