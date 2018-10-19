package com.example.demo.kafka.integrate.client.kafka.log;

public class LogTaskServer {
	public  static void startLogCollectionTask(String zookeeperUrl){
		LogReaderTask logReaderTask=new LogReaderTask(zookeeperUrl);
		new Thread(logReaderTask).start();
		new CleanLogFileTask();
	}
}
