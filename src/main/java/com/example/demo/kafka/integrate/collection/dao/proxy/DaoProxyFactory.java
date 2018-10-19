package com.example.demo.kafka.integrate.collection.dao.proxy;

import java.lang.reflect.Proxy;

import com.example.demo.kafka.integrate.collection.dao.proxy.DaoProxy;
import com.topscore.integrate.dao.MsgDao;
import com.topscore.integrate.dao.impl.MsgDaoImpl;

public class DaoProxyFactory {

	public static MsgDao buildMsgDao() {
		MsgDao dao = new MsgDaoImpl();
        DaoProxy daoProxy = new DaoProxy(dao);
        Class<?> cla = dao.getClass();
        MsgDao msgDao = (MsgDao) Proxy.newProxyInstance(cla.getClassLoader(), cla.getInterfaces(), daoProxy);
        return msgDao;
   }
}
