package com.example.demo.transaction.controller;

import com.example.demo.transaction.ServiceA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TxController {

    @Autowired
    private ServiceA serviceA;


    @RequestMapping("/tx/{status}")
    public Boolean testGuava(@PathVariable Integer status) {
        try {
            serviceA.doA(status);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }

    }
}
