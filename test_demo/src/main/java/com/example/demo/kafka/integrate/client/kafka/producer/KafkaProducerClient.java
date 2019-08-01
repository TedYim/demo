package com.example.demo.kafka.integrate.client.kafka.producer;

import java.util.Properties;

import com.example.demo.kafka.integrate.client.kafka.log.LogWriteQueue;
import com.example.demo.kafka.integrate.client.kafka.log.MsgTypeEnum;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;


public final class KafkaProducerClient {
    Producer<String, String> producer;

    private final String topic;

    public KafkaProducerClient(Properties props, String topic) {
        if (props == null) {
            System.err.println("必须配置kafka的内容");
            System.exit(0);
        }
        if (topic == null || topic.trim().length() == 0) {
            System.err.println("必须配置topic");
            System.exit(0);
        }
        this.topic = topic;
        //配置value的序列化类
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "org.apache.kafka.clients.producer.internals.DefaultPartitioner");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16 * 1024);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 32 * 1024 * 1024);

        producer = new KafkaProducer<String, String>(props);
    }

    public final void sendMessage(String messageId, String messageBody) {
        sendMessage(messageId, messageBody, "", "");
    }

    public final void sendMessage(String messageId, String messageBody, String keyword, String keyword2) {

        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, messageId, messageBody);
        producer.send(producerRecord);
        LogWriteQueue.writeLog(topic, messageId, messageBody, MsgTypeEnum.SEND, keyword, keyword2);
    }
}
