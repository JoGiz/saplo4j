package com.voltvoodoo.saplo4j.http;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.json.simple.JSONValue;

import com.voltvoodoo.saplo4j.async.SaploCallback;

public class JsonResponseHandler extends SimpleChannelUpstreamHandler {
	
	protected SaploCallback<Object> callback;
	protected Object requestData;
	
	public JsonResponseHandler(SaploCallback<Object> callback, Object requestData) {
		this.callback = callback;
		this.requestData = requestData;
	}
	
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    
		HttpResponse response = (HttpResponse) e.getMessage();
		ChannelBuffer content = response.getContent();
		
		if (content.readable()) {
			Object result = JSONValue.parse(content.toString("UTF-8"));
			
			if( result == null ) {
				System.out.println("SAPLO ERROR, unparseable response.");
				System.out.println("OUR REQUEST:");
				System.out.println(requestData);
				System.out.println("SAPLO RESPONSE:");
				System.out.println(content.toString("UTF-8"));
			}
			
			callback.onSuccess(result);
		}
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
	}
}