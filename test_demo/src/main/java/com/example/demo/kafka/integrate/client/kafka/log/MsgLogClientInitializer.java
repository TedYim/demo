package com.example.demo.kafka.integrate.client.kafka.log;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

class MsgLogClientInitializer extends ChannelInitializer<SocketChannel> {
		ByteBuf delimiter = Unpooled.copiedBuffer(LogConstant.delimiter.getBytes());
		MsgLogSendClient client=null; 
		public MsgLogClientInitializer(MsgLogSendClient client) {
			this.client=client;
		}
		@Override
	    public void initChannel(SocketChannel ch) throws Exception {
	        ChannelPipeline pipeline = ch.pipeline();
	        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(LogConstant.maxFrameLength, delimiter));
	        pipeline.addLast("decoder", new StringDecoder());
	        pipeline.addLast("encoder", new StringEncoder());
	        pipeline.addLast("heartBeat", new HeartBeatHandler(client));
	        pipeline.addLast("handler", new MsgLogClientHandler());
	    }
}
