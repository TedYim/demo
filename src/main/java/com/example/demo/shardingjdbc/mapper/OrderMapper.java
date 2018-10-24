package com.example.demo.shardingjdbc.mapper;

import com.example.demo.shardingjdbc.entity.Order;
import com.example.demo.shardingjdbc.condition.OrderQueryCondition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {

    Integer insert(Order order);

    List<Order> selectAll();

    List<Order> queryPageByCondition(OrderQueryCondition condition);
}
