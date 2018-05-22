package com.example.demo;

import com.example.demo.kafka.producer.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableScheduling
public class DemoConfigurationApplicationTests {

    @Autowired
    private Sender sender;

    @Test
    public void contextLoads() {
        for (int i = 0; i < 10; i++) {
            sendMsg();
        }
    }

    //@Scheduled(fixedRate = 1000 * 60)
    private void sendMsg() {
        sender.send("helloworld", "testValue- [ " + new Date() + " ]");
        System.out.println("发送的数据为 testValue- [ " + new Date() + " ]");
    }

}
