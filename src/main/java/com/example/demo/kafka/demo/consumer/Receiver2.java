package com.example.demo.kafka.demo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class Receiver2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver2.class);

    //private CountDownLatch latch = new CountDownLatch(1);

    //public CountDownLatch getLatch() {
    //  return latch;
    //}
    private int i = 0;


    /**
     * 通过制定containerFactory来指定对应的groupid来进行广播消费
     * @param payload
     */
    @KafkaListener(topics = "${kafka.topic.helloworld}", containerFactory = "kafkaListenerContainerFactory2")
    public void receive2(String payload) {
        LOGGER.info("接收到的数据 , group = 2'", payload);
        //latch.countDown();
        i++;
        System.err.println(" group = 2 一共接收到数据数量为" + i);
    }
}
