package com.example.demo.kafka.integrate.client.kafka.log;


public interface LogConstant {
	/****
	 * nas磁盘上的路径
	 */
	 String logDir="/data/kafka/log";
	 
	 String segmentation="@#segment#@";
	 
	 String delimiter="@@##@@";
	int maxFrameLength=3*1024*1024;//3m
}
