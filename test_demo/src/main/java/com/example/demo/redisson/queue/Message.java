package com.example.demo.redisson.queue;

import lombok.Data;

import java.io.Serializable;
@Data
public class Message implements Serializable {

    public Message() {
    }

    public Message(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    private Long   id;
    private String content;
}
