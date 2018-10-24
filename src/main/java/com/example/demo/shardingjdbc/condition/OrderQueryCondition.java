package com.example.demo.shardingjdbc.condition;

import lombok.Data;


@Data
public class OrderQueryCondition {

    private Integer pageNum;

    private Integer pageSize;

    private Integer userId;
}
