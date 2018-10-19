package com.example.demo.kafka.integrate.client.kafka.log;

import com.example.demo.kafka.integrate.client.netty.Header;
import com.example.demo.kafka.integrate.client.netty.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

 
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;
    Logger logger=LoggerFactory.getLogger(HeartBeatHandler.class);
    MsgLogSendClient client;
    public HeartBeatHandler(MsgLogSendClient client) {
    	this.client=client;
	}
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	logger.info("激活时间是："+new Date(System.currentTimeMillis()));  
        ctx.fireChannelActive();  
		heartBeat = ctx.executor().scheduleAtFixedRate(
		    new HeartBeatHandler.HeartBeatTask(ctx), 0, 3000,
		    TimeUnit.MILLISECONDS);
    }
    
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	try{
    	  ObjectMapper mapper=new ObjectMapper();
    	  NettyMessage message= mapper.readValue(msg.toString(),NettyMessage.class);
			if(message.getHeader()==null){
				ctx.fireChannelRead(message);
			}
			 if (message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
				logger.info("接受到服务器心跳时间"+new Date(System.currentTimeMillis()));
			}else{
				ctx.fireChannelRead(message);
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	 }
    private class HeartBeatTask implements Runnable {
    	private final ChannelHandlerContext ctx;

    	public HeartBeatTask(final ChannelHandlerContext ctx) {
    	    this.ctx = ctx;
    	}

    	@Override
    	public void run() {
    	    NettyMessage heatBeat = buildHeatBeat();
            ObjectMapper mapper=new ObjectMapper();
            String msg="";
			try {
				msg = mapper.writeValueAsString(heatBeat);
				ctx.writeAndFlush(msg+LogConstant.delimiter);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
    	}
	    private NettyMessage buildHeatBeat() {
	    	NettyMessage message = new NettyMessage();
	    	Header header = new Header();
	    	header.setType(MessageType.HEARTBEAT_REQ.value());
	    	message.setHeader(header);
	    	return message;
	    }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	    throws Exception {
		cause.printStackTrace();
		ctx.fireExceptionCaught(cause);
		if (heartBeat != null) {
		    heartBeat.cancel(true);
		    heartBeat = null;
		}
		client.failover();
    }
}
