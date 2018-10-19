package com.example.demo.kafka.integrate.collection.dao.impl;

import com.topscore.integrate.dao.MsgDao;
import com.topscore.integrate.entity.MsgEntity;

public class MsgDaoImpl extends BaseDaoImpl implements MsgDao {

    @Override
    public MsgEntity getByRowKey(String rowKey) {
        MsgEntity msgEntity = super.get(rowKey,MsgEntity.class);
        return msgEntity;
    }

    @Override
    public void put(MsgEntity msgEntity) {
        String rowKey=msgEntity.getId();
        super.save(rowKey,msgEntity);
    }
}
