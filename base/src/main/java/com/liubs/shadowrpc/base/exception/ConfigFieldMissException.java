package com.liubs.shadowrpc.base.exception;

/**
 * 配置字段不完整异常
 * @author Liubsyy
 * @date 2024/1/23
 **/
public class ConfigFieldMissException extends RuntimeException{
    public ConfigFieldMissException(String message) {
        super(message);
    }
}
