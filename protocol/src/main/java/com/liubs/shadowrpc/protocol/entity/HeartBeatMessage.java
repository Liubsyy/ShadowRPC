package com.liubs.shadowrpc.protocol.entity;

/**
 * 心跳消息
 * 只用一个字节 (byte)1 即可
 * @author Liubsyy
 * @date 2023/12/15 9:42 PM
 **/
public class HeartBeatMessage {

    public static final byte PING_MESSAGE = (byte)1;
    public static final byte[] PING_BYTE = new byte[]{PING_MESSAGE};

    public static HeartBeatMessage SINGLETON = new HeartBeatMessage();
    private HeartBeatMessage(){}

    public static boolean isHeartBeatMsg(byte[] bytes) {
        return null != bytes && bytes.length == 1 && bytes[0] == PING_MESSAGE;
    }

    public static byte[] getHearBeatMsg() {
        return PING_BYTE;
    }

}
