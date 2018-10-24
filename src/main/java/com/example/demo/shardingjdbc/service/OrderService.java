package com.example.demo.shardingjdbc.service;

import com.example.demo.shardingjdbc.entity.Order;
import com.example.demo.shardingjdbc.condition.OrderQueryCondition;
import com.github.pagehelper.PageInfo;

public interface OrderService {

    PageInfo<Order> queryByPage(OrderQueryCondition condition);
}
