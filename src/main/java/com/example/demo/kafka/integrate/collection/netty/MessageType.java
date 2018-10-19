package com.example.demo.kafka.integrate.collection.netty;
public enum MessageType {

    SERVICE_REQ((byte) 0), SERVICE_RESP((byte) 1), ONE_WAY((byte) 2), HEARTBEAT_REQ((byte) 5), HEARTBEAT_RESP(
	    (byte) 6);

    private byte value;

    private MessageType(byte value) {
	this.value = value;
    }

    public byte value() {
	return this.value;
    }
}
