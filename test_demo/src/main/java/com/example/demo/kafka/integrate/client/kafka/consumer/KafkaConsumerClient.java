package com.example.demo.kafka.integrate.client.kafka.consumer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.demo.kafka.integrate.client.kafka.log.LogWriteQueue;
import com.example.demo.kafka.integrate.client.kafka.log.MsgTypeEnum;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KafkaConsumerClient implements Runnable {
    private Consumer<String, String> consumer;
    private KafkaConsumerListener consumerListener = null;
    static  ExecutorService       fixedThreadPool  = Executors.newFixedThreadPool(3);
    static  Logger                logger           = LoggerFactory.getLogger(KafkaConsumerClient.class);
    private String topic;

    /**
     * @param topic
     * @param bootstrapServers
     * @param groupId
     */
    public KafkaConsumerClient(String topic, String bootstrapServers, String groupId) {
        this.consumer = new KafkaConsumer<String, String>(createConsumerConfig(bootstrapServers, groupId));
        this.topic = topic;
        consumer.subscribe(Arrays.asList(topic),

                /**
                 * 此处可以实现本地管理offset
                 * onPartitionsRevoked(java.util.Collection<TopicPartition> partitions)里保存当前的partition和offset到数据库中。
                 * 然后reassign完成后，void onPartitionsAssigned(java.util.Collection partitions)中从数据库读取之前的消费位置，通过seek方法设置消费位置继续消费。
                 */
                new ConsumerRebalanceListener() {

                    @Override
                    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                        // 分区撤销事件
                        consumer.commitSync();// 提交位移到brokers , 等同于enable.auto.commit = true
                    }

                    @Override
                    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                        // 分区分配事件
                        long committedOffset = -1;
                        for (TopicPartition topicPartition : partitions) {
                            if (topicPartition == null) {
                                continue;
                            }
                            committedOffset = consumer.committed(topicPartition).offset();// 获取该分区已消费的位移

                            consumer.seek(topicPartition, committedOffset + 1);// 重置位移到上一次提交的位移处开始消费
                            //consumer.seek(topicPartition, 0);// 设置为0之后就从头开始消费
                        }
                    }
                });
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                String value = record.value();
                fixedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        feedbackMsg(record.key(), value);//只是记录数据
                        noticeConsumer(value);//对外处理数据
                    }
                });
            }
        }

    }

    private Properties createConsumerConfig(String bootstrapServers, String groupId) {
        // 1. 构建属性对象
        Properties prop = new Properties();
        // 2. 添加相关属性
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, groupId); // 指定分组id
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");//不自动提交
        prop.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); //必须要加要读旧数据


        return prop;
    }

    public void noticeConsumer(String message) {
        if (consumerListener != null) {
            consumerListener.noticeConsumer(message);
        }
    }

    public void feedbackMsg(String key, String message) {
        LogWriteQueue.writeLog(topic, key, message, MsgTypeEnum.RECEIVED);
    }

    /**
     * Kafka消费者数据处理线程
     */

    public void setKafkaConsumerListener(KafkaConsumerListener consumerListener) {
        this.consumerListener = consumerListener;
    }
}