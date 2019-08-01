package com.example.demo.kafka.integrate.client.kafka.log;

import com.example.demo.kafka.integrate.client.netty.Header;
import com.example.demo.kafka.integrate.client.netty.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.util.Map;


 class MsgLogClientHandler extends  ChannelInboundHandlerAdapter implements LogConstant{
	@Override
	public void channelRead(ChannelHandlerContext context, Object msg)
			throws Exception {
		NettyMessage message= (NettyMessage)msg;
    	if(message==null){
    		  return;
    	}
    	Header header=message.getHeader();
		if(header==null||MessageType.SERVICE_RESP.value()!=message.getHeader().getType()){
			context.fireChannelRead(msg);
		}
		Map<String, String> attributeMap=header.getAttributeMap();
		try{
			if(attributeMap==null){
				return;
			}
			String messageId=attributeMap.get("messageId");
			String topic=attributeMap.get("topic");
			String operationEnumName=attributeMap.get("operationEnumName");
			String ip=attributeMap.get("ip");
			File file=new File(logDir, messageId+segmentation+topic+segmentation+operationEnumName+segmentation+ip);
			
			if(file.exists()){
				file.delete();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
