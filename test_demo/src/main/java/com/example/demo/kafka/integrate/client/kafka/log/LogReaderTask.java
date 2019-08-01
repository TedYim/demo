package com.example.demo.kafka.integrate.client.kafka.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.kafka.integrate.client.netty.Header;
import com.example.demo.kafka.integrate.client.netty.NettyMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class LogReaderTask implements LogConstant,Runnable{
    
    private ExecutorService executorPool;
    private int numThreads=5;
    private final String zookeeperUrl;
	List<SendMsgTask> sendMsgTaskList=new ArrayList<SendMsgTask>();
	Vector<String> filenameVector=new Vector<String>();
	Vector<String> sendedVector=new Vector<String>();
	public LogReaderTask(String zookeeperUrl) {
		executorPool=Executors.newFixedThreadPool(this.numThreads);
		this.zookeeperUrl=zookeeperUrl;
		for(int i=0;i<numThreads;i++){
			executorPool.execute(new SendMsgTask());
		}
	}
	
	class SendMsgTask implements Runnable{
		
		Vector<File> files=new Vector<File>();
		MsgLogSendClient client=null;
		public SendMsgTask() {
			sendMsgTaskList.add(this);
			client=new MsgLogSendClient(zookeeperUrl);
		}
		@Override
		public void run() {
			while(true){
				try{
					if(files.isEmpty()){
						Thread.sleep(100);
						continue;
					}
					File f=files.remove(0);
					String filename=f.getName();
					String temp[]=filename.split(segmentation);
					String msgId=temp[0];
					String topic=temp[1];
					String operationType=temp[2];
					String ip=temp[3];
					
					FileInputStream inputStream=new FileInputStream(f);
					InputStreamReader reader=new InputStreamReader(inputStream,"UTF-8");
					BufferedReader bufferedReader=new BufferedReader(reader);
					String line=null;
					StringBuffer buffer=new StringBuffer();
					while((line=bufferedReader.readLine())!=null){
						if(buffer.length()>0){
							buffer.append("\r\n");
						}
						buffer.append(line);
					}
					
					JSONObject jsonObject=JSONObject.parseObject(buffer.toString());
					if(jsonObject==null){
						continue;
					}
					String messageBody=jsonObject.getString("messageBody");
					String keyword=jsonObject.getString("keyword");
					String keyword2=jsonObject.getString("keyword2");
					String recordTime=jsonObject.getString("recordTime");
					LogModel model=new LogModel(topic,msgId, messageBody,MsgTypeEnum.valueOf(operationType));
					model.setIp(ip);
					model.setRequestTime(System.currentTimeMillis());
					model.setRecordTime(Long.valueOf(recordTime));
					model.setKeyword(keyword);
					model.setKeyword2(keyword2);
					NettyMessage message=buildMsg(model);
					ObjectMapper mapper=new ObjectMapper();
					String data=mapper.writeValueAsString(message);
					client.send(data);
					bufferedReader.close();
					sendedVector.add(f.getName());
 				}catch(Exception e){
 					if(!(e instanceof FileNotFoundException)){
 						e.printStackTrace();
 					}
				}
			}
		}
	}
	private NettyMessage buildMsg(LogModel body) {
	    NettyMessage message = new NettyMessage();
	    Header header = new Header();
	    header.setType(MessageType.SERVICE_REQ.value());
	    message.setHeader(header);
	    message.setBody(body);
	    return message;
	}
	@Override
	public void run() {
		int count=0;
		int maxReadCount=60;
		while(true){
			File file=new File(logDir);
			File[] files=file.listFiles();
			if(files==null||files.length==0){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			int size=sendMsgTaskList.size();
			for(File f:files){
				SendMsgTask msgTask=sendMsgTaskList.get(new Random().nextInt(size));
				if(!filenameVector.contains(f.getName())){
					filenameVector.add(f.getName());
					msgTask.files.add(f);
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(++count>maxReadCount){
				filenameVector.removeAll(sendedVector);
				count=0;
			}
		}
	}
	
	
}
