package com.example.demo;

import com.example.demo.spring_event.code.EventDemo;
import com.example.demo.spring_event.code.TestPublishCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventPublishTest {

    @Test
    public void test() throws Exception {
        com.example.demo.spring_event.anno.EventDemo eventDemo1 = new com.example.demo.spring_event.anno.EventDemo(null);
        EventDemo eventDemo2 = new EventDemo(null);
        TestPublishCode.publishEvent(eventDemo1);
        TestPublishCode.publishEvent(eventDemo2);
    }
}
