package com.example.demo.kafka.consumer;

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Created by Ted on 2018/5/22.
 */
public class Group2Listener {

    @KafkaListener(topics = {"taskCmd"})
    public void taskCmd(ConsumerRecord<?, ?> record) {
        System.out.println("  KafkaConsumer ---------->>>>>>>>:" + JSONUtils.toJSONString(record));
        Object message = record.value();
        System.out.println(" 这是group2 topic taskCmd " + record.topic());
        System.out.println(message);
        System.out.println(record.key());
        System.out.println(record);
    }

    @KafkaListener(topics = {"task"})
    public void task(ConsumerRecord<?, ?> record) {
        System.out.println("这是group2 topic task KafkaConsumer ---------->>>>>>>>:" + JSONUtils.toJSONString(record));
        Object message = record.value();
        System.out.println("这是group2 topic task " + record.topic());
        System.out.println(message);
        System.out.println(record.key());
        System.out.println(record);
    }

    @KafkaListener(topics = {"task1"}, group = "group2")
    public void task1(ConsumerRecord<?, ?> record) {
        System.out.println("这是group2" + " task1 的消费者");
        System.out.println("这是group2 topic task1 KafkaConsumer ---------->>>>>>>>:" + JSONUtils.toJSONString(record));
        Object message = record.value();
        System.out.println("group2 topic task1 " + record.topic());
        System.out.println(message);
        System.out.println(record.key());
        System.out.println(record);
    }

    @KafkaListener(topics = {"gift"}, group = "group2")
    public void gift(ConsumerRecord<String, String> record) {

        String key = record.key();
        String value = record.value();

        System.out.println("groupId2 kafka gift Consumer value:" + value);

    }
}
