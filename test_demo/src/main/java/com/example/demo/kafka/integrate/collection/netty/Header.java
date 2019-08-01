package com.example.demo.kafka.integrate.collection.netty;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
@Data
public final class Header {

    private byte type;// 消息类型

    private Map<String, String> attributeMap = new HashMap<String, String>(); 
     
}
