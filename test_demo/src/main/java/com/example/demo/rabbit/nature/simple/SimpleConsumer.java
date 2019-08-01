package com.example.demo.rabbit.nature.simple;

import com.example.demo.rabbit.nature.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

public class SimpleConsumer {
    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionUtils.getConn();
        Channel channel = conn.createChannel();

        channel.queueDeclare("simple_queue_name", false, false, false, null);

        //通过事件回调的方式消费
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println(msg);
            }
        };
        channel.basicConsume("simple_queue_name", true, defaultConsumer);
        channel.close();
        conn.close();
    }
}
