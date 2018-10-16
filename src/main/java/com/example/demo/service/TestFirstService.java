package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ted on 2018/9/20.
 */
@Service
@Lazy
public class TestFirstService {

    @Autowired
    public TestFirstService(CommonService commonService, ThreadPoolTaskExecutor taskExecutor) {
        this.commonService = commonService;
        this.taskExecutor = taskExecutor;
        doThings();
    }

    private CommonService commonService;

    private ThreadPoolTaskExecutor taskExecutor;


    public void doThings() {
        System.err.println("test init ...");
        List<Integer> msg = this.commonService.getMsg(true);


        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (Integer i : msg) {
                        //System.out.println("第一层遍历 : " + i);

                    }
                }
            }
        });
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Integer> msg1 = this.commonService.getMsg(false);
        System.out.println(msg == msg1);
        System.out.println(msg.equals(msg1));
        while (true) {
            for (Integer i : msg1) {
                //System.out.println("第二层遍历 : " + i);
            }
        }


    }
}
