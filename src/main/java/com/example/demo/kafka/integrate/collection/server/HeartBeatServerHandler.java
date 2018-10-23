package com.example.demo.kafka.integrate.collection.server;

import com.example.demo.kafka.integrate.collection.kafka.log.LogConstant;
import com.example.demo.kafka.integrate.collection.netty.Header;
import com.example.demo.kafka.integrate.collection.netty.MessageType;
import com.example.demo.kafka.integrate.collection.netty.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter  {
	
	static Logger logger=LoggerFactory.getLogger(HeartBeatServerHandler.class);
	    
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
		ObjectMapper mapper=new ObjectMapper();
    	NettyMessage message= mapper.readValue(msg.toString(),NettyMessage.class);
		if(message.getHeader()==null){
			  ctx.fireChannelRead(message);
		}
		Header header=message.getHeader();
		// 返回心跳应答消息
		if (header.getType() == MessageType.HEARTBEAT_REQ.value()) {
		    NettyMessage heartBeat = buildHeatBeat();
		    ctx.writeAndFlush(mapper.writeValueAsString(heartBeat)+ LogConstant.segmentation);
		} else{
			ctx.fireChannelRead(message);
		}
	}

    private NettyMessage buildHeatBeat() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP.value());
		message.setHeader(header);
		return message;
    }
}
