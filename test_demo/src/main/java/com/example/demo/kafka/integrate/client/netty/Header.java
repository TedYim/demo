package com.example.demo.kafka.integrate.client.netty;

import java.util.HashMap;
import java.util.Map;
 
public final class Header {

    private byte type;

    private Map<String, String> attributeMap = new HashMap<String, String>();

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	} 
    
    
}
