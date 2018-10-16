package com.example.demo.path;

import java.io.File;
public class Demo {

    private String getTemplatesPath() {
        String path = System.getProperty("user.dir") + File.separator + " classes " + File.separator + "templates";
        File file = new File(path);
        if (!file.isDirectory()) {
            path = this.getClass().getResource("/templates").getPath();
        }
        System.out.println(path + "============文件的路径============================");
        return path;
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.getTemplatesPath();
    }
}
