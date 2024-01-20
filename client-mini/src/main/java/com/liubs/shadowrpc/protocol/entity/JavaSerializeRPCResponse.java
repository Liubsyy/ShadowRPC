package com.liubs.shadowrpc.protocol.entity;

import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2024/1/20
 **/
public class JavaSerializeRPCResponse implements Serializable {

    private String traceId;

    private int code;

    private String errorMsg;

    private Object result;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
