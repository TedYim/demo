package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Service
public class TimeService {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String getTime() {
        return restTemplate.getForEntity("http://time-service", String.class).getBody();
    }
}
