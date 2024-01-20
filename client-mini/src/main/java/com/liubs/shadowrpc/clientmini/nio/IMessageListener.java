package com.liubs.shadowrpc.clientmini.nio;

/**
 * 接收消息处理
 * @author Liubsyy
 * @date 2024/1/20
 **/
public interface IMessageListener {
    void handleMessage(byte[] bytes);
}
