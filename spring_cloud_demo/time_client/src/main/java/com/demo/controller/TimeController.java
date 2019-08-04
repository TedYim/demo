package com.demo.controller;

import com.demo.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeController {

    @Autowired
    private TimeService timeService;

    @GetMapping("/time")
    public String getTime() {
        return timeService.getTime();
    }
}
