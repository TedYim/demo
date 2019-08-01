package com.example.demo.rabbit.nature.work;

import com.example.demo.rabbit.nature.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WorkConsumer {
    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionUtils.getConn();
        Channel channel = conn.createChannel();

        channel.queueDeclare("work_queue_name", false, false, false, null);

        //自动确认1条
        channel.basicQos(1);

        //通过事件回调的方式消费
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println(msg);

                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }

            }
        };

        channel.basicConsume("work_queue_name", false, defaultConsumer);

        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newSingleThreadExecutor();
        executorService.prestartAllCoreThreads();//可预先创建corePoolSize数量的核心线程
        executorService.allowCoreThreadTimeOut(true);
    }
}
