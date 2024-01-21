package com.liubs.shadowrpc.clientmini.exception;

/**
 * 远程服务器已经关闭
 * @author Liubsyy
 * @date 2024/1/21
 **/
public class RemoteClosedException extends RuntimeException{

    public RemoteClosedException(String message) {
        super(message);
    }
}
