package com.example.demo.kafka.integrate.collection.dao.proxy;

import java.lang.reflect.Proxy;

import com.example.demo.kafka.integrate.collection.dao.MsgDao;
import com.example.demo.kafka.integrate.collection.dao.impl.MsgDaoImpl;

public class DaoProxyFactory {

	public static MsgDao buildMsgDao() {
		MsgDao dao = new MsgDaoImpl();
        DaoProxy daoProxy = new DaoProxy(dao);
        Class<?> cla = dao.getClass();
        MsgDao msgDao = (MsgDao) Proxy.newProxyInstance(cla.getClassLoader(), cla.getInterfaces(), daoProxy);
        return msgDao;
   }
}
