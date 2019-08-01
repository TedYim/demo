package com.example.demo.delayqueue;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

public class Test {

    public static void main(String[] args) {

        //TaskMessage taskMessage = new TaskMessage(1 * 1000L, "testBody", System.out::println);
        //DelayQueue<TaskMessage> queue = taskManager.getQueue();
        //queue.offer(taskMessage);
        Sync sync1 = new Sync("1","1");
        Sync sync2 = new Sync("2","2");
        List<Sync> syncs = Lists.newArrayList();
        syncs.add(sync1);
        syncs.add(sync2);
        for (Sync sync : syncs) {
            if (sync.getKey().equals("1")){
                sync.setValue("3");
            }
        }

        for (Sync sync : syncs) {
            System.out.println(sync);
        }


    }

    @Data
    static class Sync {
        private String key;
        private String value;

        public Sync(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
