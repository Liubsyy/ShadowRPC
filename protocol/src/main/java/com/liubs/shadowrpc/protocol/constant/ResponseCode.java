package com.liubs.shadowrpc.protocol.constant;

/**
 * @author Liubsyy
 * @date 2023/12/23 1:47 PM
 **/
public enum ResponseCode {

    SUCCESS(10,"成功"),
    FAIL(40,"失败"),

    ;
    private int code;
    private String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
