package com.example.demo.shardingjdbc.entity;

import lombok.Data;

@Data
public class OrderItem {

    private Integer itemId;
    private Integer orderId;
    private Integer userId;
}
