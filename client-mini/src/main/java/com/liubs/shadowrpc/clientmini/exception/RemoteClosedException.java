package com.liubs.shadowrpc.clientmini.exception;

/**
 * 收到这个异常，表示本地已经关闭和远程的socket连接（一般是服务器已经断开）
 * @author Liubsyy
 * @date 2024/1/21
 **/
public class RemoteClosedException extends Exception{

    public RemoteClosedException(String message) {
        super(message);
    }
}
