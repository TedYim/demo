package com.example.demo.shardingjdbc.service.impl;

import com.example.demo.shardingjdbc.entity.Order;
import com.example.demo.shardingjdbc.condition.OrderQueryCondition;
import com.example.demo.shardingjdbc.mapper.OrderMapper;
import com.example.demo.shardingjdbc.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public PageInfo<Order> queryByPage(OrderQueryCondition condition) {
        Map<String, Object> data = Maps.newHashMap();
        Integer pageNum = condition.getPageNum() != null ? condition.getPageNum() : 1;
        Integer pageSize = condition.getPageSize() != null ? condition.getPageSize() : 10;
        PageHelper.startPage(pageNum, pageSize, true);
        List<Order> orders = orderMapper.queryPageByCondition(condition);
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        return pageInfo;
    }
}
