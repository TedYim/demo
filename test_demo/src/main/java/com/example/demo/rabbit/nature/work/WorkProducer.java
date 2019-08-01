package com.example.demo.rabbit.nature.work;

import com.example.demo.rabbit.nature.utils.ConnectionUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.messaging.Message;

public class WorkProducer {


    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionUtils.getConn();
        Channel channel = conn.createChannel();

        channel.queueDeclare("work_queue_name", false, false, false, null);

        for (int i = 0; i < 100; i++) {
            String msgBody = "work_queue_msg" + i;
            AMQP.BasicProperties properties = new AMQP.BasicProperties();
            AMQP.BasicProperties basicProperties = properties.builder().priority(1).build();
            channel.basicPublish("", "work_queue_name", properties, msgBody.getBytes());

        }
        channel.close();
        conn.close();
    }
}
