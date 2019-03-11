package com.example.demo.io.direct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class IOTest {
    static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        mmap();
        //directIO();

    }

    /**
     * 测试mmap,2200ms
     * @throws Exception
     */
    private static void mmap() throws Exception {
        File file = new File("E:\\SAP安装包\\SAPGui730.zip");
        FileInputStream in = new FileInputStream(file);
        FileChannel channel = in.getChannel();
        MappedByteBuffer buff = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

        byte[] b = new byte[1024];
        int len = (int) file.length();

        long begin = System.currentTimeMillis();

        for (int offset = 0; offset < len; offset += 1024) {

            if (len - offset > BUFFER_SIZE) {
                buff.get(b);
            } else {
                buff.get(new byte[len - offset]);
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("time is:" + (end - begin));
    }

    /**
     * ByteBuffer.allocateDirect(1024); 4350ms
     * 测试ByteBuffer.allocateDirect((int)file.length());  384ms
     * @throws Exception
     */
    private static void directIO() throws Exception {
        File file = new File("E:\\SAP安装包\\SAPGui730.zip");
        FileInputStream in = new FileInputStream(file);
        FileChannel channel = in.getChannel();
        ByteBuffer buff = ByteBuffer.allocateDirect(1024);
        //ByteBuffer buff = ByteBuffer.allocateDirect((int)file.length());

        long begin = System.currentTimeMillis();
        while (channel.read(buff) != -1) {
            buff.flip();
            buff.clear();
        }
        long end = System.currentTimeMillis();
        System.out.println("time is:" + (end - begin));
    }

}
