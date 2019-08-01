package com.example.demo.spring_event.code;

import org.springframework.context.ApplicationEvent;

/**
 * 定义一个事件
 */
public class EventDemo extends ApplicationEvent {

    /**
     * 事件内有需要操作的对象
     */
    private TestParam source;

    public EventDemo(TestParam source) {
        super(source);
        this.source = source;
    }

    /**
     * 触发事件的时候需要拿到要操作的对象
     * @return
     */
    @Override
    public TestParam getSource() {
        return source;
    }
}
