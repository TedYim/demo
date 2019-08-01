package com.example.demo.kafka.integrate.collection.dao;


import com.example.demo.kafka.integrate.collection.entity.MsgEntity;

public interface MsgDao extends BaseDao {
    MsgEntity getByRowKey(String rowKey);

    void put(MsgEntity msgEntity);
}
