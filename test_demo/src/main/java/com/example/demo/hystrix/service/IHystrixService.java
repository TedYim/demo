package com.example.demo.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

public interface IHystrixService {

    String getSth(String str);

    String defaultStr(String str, Throwable e);

}
