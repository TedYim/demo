package com.example.demo.mapper;

import com.example.demo.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {

    Integer insert(Order order);

    List<Order> selectAll();
}
