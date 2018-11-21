package com.example.demo.spring_event.code;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Ted on 2018/11/21.
 */
public class EventDemo extends ApplicationEvent {

    private TestParam source;

    public EventDemo(TestParam source) {
        super(source);
        this.source = source;
    }
}
