package com.example.demo.transaction.impl;

import com.example.demo.transaction.ServiceA;
import com.example.demo.transaction.ServiceB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;


@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service("serviceA")
public class ServiceAImpl implements ServiceA {

    @Autowired
    private ServiceB serviceB;

    @Override
    public void doA(Integer status) {
        log.info("===================== ServiceA Started =======================");

        //doRegister();
        log.info("===================== ServiceA register event =======================");
        serviceB.doB(status);
        log.info("===================== ServiceA Ended =======================");
    }

    private void doRegister() {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                //do what you want to do after commit
                log.warn("-------------------- afterCommit ----------------------");
            }

            @Override
            public void afterCompletion(int status){
                log.warn("-------------------- afterCompletion , status is [{}]  ----------------------", status);
            }

        });
    }

}
