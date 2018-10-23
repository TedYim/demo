package com.example.demo.kafka.integrate.collection.service;


import com.example.demo.kafka.integrate.collection.kafka.log.LogModel;

/**
 * Created by lkl on 2017/12/8.
 */
public interface MsgService {
    void saveMsg(LogModel logModel);
}
