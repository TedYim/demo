package com.demo.controller;

import com.demo.service.StockInterfaceService;
import com.demo.service.TimeFeignService;
import com.demo.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeController {

    @Autowired
    private TimeService timeService;

    @Autowired
    private TimeFeignService timeFeignService;

    @Autowired
    private StockInterfaceService stockInterfaceService;

    @GetMapping("/time")
    public String getTime() {
        return timeService.getTime();
    }

    @GetMapping("/feign")
    public String getFeign() {
        return timeFeignService.getFeignTime();
    }

    @GetMapping("/stock")
    public String getStockInfo() {
        return "通过SpringCloud调用RestTemplate结果为 : " + stockInterfaceService.getStatus();
    }
}
