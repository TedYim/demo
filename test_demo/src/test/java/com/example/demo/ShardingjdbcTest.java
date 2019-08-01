package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.shardingjdbc.entity.Order;
import com.example.demo.shardingjdbc.condition.OrderQueryCondition;
import com.example.demo.shardingjdbc.mapper.OrderMapper;
import com.example.demo.shardingjdbc.service.OrderService;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingjdbcTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Test
    public void insertTest() throws Exception {
        for (int i = 0; i < 100; i++) {
            Order order = new Order();
            if (i % 2 == 0) {
                order.setUserId(10);
            } else {
                order.setUserId(11);
            }
            //order.setUserId(i);
            orderMapper.insert(order);
            System.out.println("自动生成的ID为 : " + order.getOrderId());
        }

    }

    @Test
    public void selectAllTest() throws Exception {

        List<Order> orders = orderMapper.selectAll();
        for (int i = 0; i < orders.size(); i++) {
            System.out.println("第 " + i + " 个订单 : " + orders.get(i));
        }
    }

    @Test
    public void selectPage() throws Exception {
        OrderQueryCondition condition = new OrderQueryCondition();
        condition.setUserId(1);
        //condition.setPageNum();
        PageInfo<Order> pageInfo = orderService.queryByPage(condition);
        System.err.println(JSON.toJSONString(pageInfo));
    }

}
