package com.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    public static Boolean isCanLinkDb = true;

    @RequestMapping(value = "/linkDb/{can}", method = RequestMethod.GET)
    public void LinkDb(@PathVariable Boolean can) {
        isCanLinkDb = can;
    }
}
