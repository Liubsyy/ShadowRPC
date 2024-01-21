package com.liubs.shadowrpc.clientmini.exception;

/**
 * 连接超时
 * @author Liubsyy
 * @date 2024/1/21
 **/
public class ConnectTimeoutException extends Exception{
    public ConnectTimeoutException(String s) {
        super(s);
    }
}
