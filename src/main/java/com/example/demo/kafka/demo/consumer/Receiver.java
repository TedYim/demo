package com.example.demo.kafka.demo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    //private CountDownLatch latch = new CountDownLatch(1);

    //public CountDownLatch getLatch() {
    //  return latch;
    //}
    private int i1 = 0;

    @KafkaListener(topics = "${kafka.topic.helloworld}", containerFactory = "kafkaListenerContainerFactory1")
    public void receive(String payload) {
        LOGGER.info("接收到的数据 , group = 1 [ {} ]", payload);
        //latch.countDown();
        i1++;
        System.err.println("group = 1 一共接收到数据数量为" + i1);
    }

}
