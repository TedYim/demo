package com.example.demo.kafka.integrate.collection.service.factory;

import com.topscore.integrate.service.MsgService;
import com.topscore.integrate.service.impl.MsgServiceImpl;

public class ServiceFactory {
	public static MsgService buildMsgService() {
		MsgService msgService = new MsgServiceImpl();
        return msgService;
   }
}
