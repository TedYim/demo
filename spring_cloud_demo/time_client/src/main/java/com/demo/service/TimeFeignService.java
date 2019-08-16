package com.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "time-service")
public interface TimeFeignService {

    @RequestMapping(value = "/feign", method = RequestMethod.GET)
    String getFeignTime();
}
