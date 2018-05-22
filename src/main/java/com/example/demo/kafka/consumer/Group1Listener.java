package com.example.demo.kafka.consumer;

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

/**
 * Created by Ted on 2018/5/22.
 */
public class Group1Listener {

    @KafkaListener(topics = {"test-topic"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            System.out.println("listen1 " + message);
        }
    }

    @KafkaListener(topics = {"task1"}, group = "group1")
    public void task1(ConsumerRecord<?, ?> record) {
        System.out.println("这是" + " task1 的消费者");
        System.out.println("这是group1 topic task1 KafkaConsumer ---------->>>>>>>>:" + JSONUtils.toJSONString(record));
        Object message = record.value();
        System.out.println("group1 topic task1 " + record.topic());
        System.out.println(message);
        System.out.println(record.key());
        System.out.println(record);
    }


    @KafkaListener(topics = {"gift"}, group = "group1")
    public void gift(ConsumerRecord<String, String> record) {

        String key = record.key();
        String value = record.value();

        System.out.println("groupId1 kafka gift Consumer value:" + value);

    }

}

