package com.example.demo.asyncread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceUtil {

    static ExecutorService service = Executors.newFixedThreadPool(20);
}
