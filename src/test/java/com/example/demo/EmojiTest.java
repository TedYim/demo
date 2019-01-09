package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class EmojiTest {

    public static JSONObject emojiFilter(String obj) {
        byte[] conByte = obj.getBytes();
        for (int i = 0; i < conByte.length; i++) {
            if ((conByte[i] & 0xF8) == 0xF0) {
                System.err.println("此报文包含emoji表情!");
                for (int j = 0; j < 4; j++) {
                    conByte[i + j] = 0x7E;
                }
                i += 3;
            }
        }
        String content = new String(conByte).replaceAll("~~~~", "");
        return JSON.parseObject(content) ;
    }

    public static void main(String[] args) {
        JSONObject jsonObject = emojiFilter("{\"memberName\":\"小小郑\uD83D\uDE00\uD83D\uDE01\uD83D\uDE02\"}");
        System.out.println(jsonObject);
    }
}
