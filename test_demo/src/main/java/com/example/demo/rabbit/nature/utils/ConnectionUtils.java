package com.example.demo.rabbit.nature.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtils {

    public static Connection getConn() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("112.74.39.120");
        factory.setPort(5672);
        factory.setVirtualHost("/Ted");
        factory.setUsername("admin");
        factory.setPassword("admin");
        return factory.newConnection();
    }
}
