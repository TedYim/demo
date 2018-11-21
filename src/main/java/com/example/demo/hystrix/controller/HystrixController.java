package com.example.demo.hystrix.controller;

import com.example.demo.hystrix.service.HystrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by Ted on 2018/11/21.
 */
@RestController
public class HystrixController {

    @Autowired
    private HystrixService hystrixService;

    @GetMapping("/Hystrix")
    public String hystrixCommand() {
        String uuid = UUID.randomUUID().toString();
        return this.hystrixService.getSth(uuid);
    }


}
