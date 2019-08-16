package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockInterfaceService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * http://localhost:8080/stock-interface/server/check
     * @return
     */
    public Boolean getStatus() {
        return restTemplate.getForEntity("http://STOCK-INTERFACE/stock-interface/server/check", Boolean.class).getBody();
    }

}
