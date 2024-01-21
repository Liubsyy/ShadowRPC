package com.liubs.shadowrpc.clientmini.exception;

/**
 * 写入channel超时异常
 * @author Liubsyy
 * @date 2024/1/21
 **/
public class WriteTimeoutException extends Exception{

    public WriteTimeoutException(String message) {
        super(message);
    }

    public WriteTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
