package com.example.demo.kafka.integrate.collection.kafka.log;

import lombok.Data;

@Data
public class LogModel {
	
	String messageId;
	String messageBody;
	String topic;
	MsgTypeEnum operationEnum;
	String ip;
    Long recordTime=0L;
    Long requestTime=0L;
    String keyword="";
    String keyword2="";
	public LogModel() {
	}
	public LogModel(String topic,String messageId,String messageBody,MsgTypeEnum operationEnum) {
		this.topic=topic;
		this.messageBody=messageBody;
		this.messageId=messageId;
		this.operationEnum=operationEnum;
	}

}
