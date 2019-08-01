package com.example.demo.rabbit.nature.simple;

import com.example.demo.rabbit.nature.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class SimpleProducer {


    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionUtils.getConn();
        Channel channel = conn.createChannel();

        channel.queueDeclare("simple_queue_name", false, false, false, null);
        String msgBody = "simple_queue_msg";
        channel.basicPublish("", "simple_queue_name", null, msgBody.getBytes());

        channel.close();
        conn.close();
    }
}
