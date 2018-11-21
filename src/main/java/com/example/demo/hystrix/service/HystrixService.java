package com.example.demo.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

@Service
public class HystrixService {


    @HystrixCommand(
            groupKey = "TestGroup", //默认是类名
            commandKey = "getSth",//默认是方法名
            fallbackMethod = "defaultStr",//fallback方法
            //ignoreExceptions = {BadRequestException.class} 指定哪一类异常可以忽略,不走fallback
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),//使用线程池的方式限流
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50000"),//方法执行超时时间
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//当并发错误个数达到此阀值时(在时间窗口内)，触发隔断器
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "60000"),//滚动窗口时间长度
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),//滚动窗口的桶数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000"),//隔断器被触发后，睡眠多长时间开始重试请求
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "2"),//设置当使用ExecutionIsolationStrategy.SEMAPHORE时，方法允许的最大请求数。如果达到最大并发数时，后续请求会被拒绝。信号量应该是容器（比如Tomcat）线程池一小部分，不能等于或者略小于容器线程池大小，否则起不到保护作用。默认值：10
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "5"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
            }
    )
    public String getSth(String str) {
        System.out.println("进入普通方法!");
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "传入的参数为 : " + str;
    }

    public String defaultStr(String str, Throwable e) {
        System.err.println("执行了fallBack方法 : " + str);
        return "执行了fallBack方法!";
    }

}
