package com.example.demo.kafka.integrate.collection.service.factory;

import com.example.demo.kafka.integrate.collection.service.MsgService;
import com.example.demo.kafka.integrate.collection.service.impl.MsgServiceImpl;

public class ServiceFactory {
	public static MsgService buildMsgService() {
		MsgService msgService = new MsgServiceImpl();
        return msgService;
   }
}
