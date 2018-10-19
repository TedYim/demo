package com.example.demo.kafka.integrate.client.kafka.log;

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
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public MsgTypeEnum getOperationEnum() {
		return operationEnum;
	}
	public void setOperationEnum(MsgTypeEnum operationEnum) {
		this.operationEnum = operationEnum;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Long getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Long recordTime) {
		this.recordTime = recordTime;
	}
	public Long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeyword2() {
		return keyword2;
	}
	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}
	
}
