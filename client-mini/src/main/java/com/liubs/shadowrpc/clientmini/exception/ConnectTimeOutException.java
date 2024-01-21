package com.liubs.shadowrpc.clientmini.exception;

/**
 * 连接超时
 * @author Liubsyy
 * @date 2024/1/21
 **/
public class ConnectTimeOutException extends RuntimeException{
    public ConnectTimeOutException(String s) {
        super(s);
    }
}
