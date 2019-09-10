package com.example.demo.transaction.impl;

import com.example.demo.transaction.ServiceB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service("serviceB")
@Transactional(rollbackFor = Exception.class)
public class ServiceBImpl  implements ServiceB {

    @Override
    public void doB(Integer status) {
        doRegister();
        if (status==0){
            log.info("===================== ServiceB Completed =======================");
        } else {
            log.info("===================== ServiceB Throws Exception =======================");
            throw new RuntimeException("Service B is not OK !!!");
        }
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
