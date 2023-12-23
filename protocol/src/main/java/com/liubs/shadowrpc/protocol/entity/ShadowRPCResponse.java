package com.liubs.shadowrpc.protocol.entity;

import com.liubs.shadowrpc.protocol.annotation.ShadowEntity;
import com.liubs.shadowrpc.protocol.annotation.ShadowField;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:40 PM
 **/

@ShadowEntity
public class ShadowRPCResponse {

    @ShadowField(1)
    private String traceId;

    @ShadowField(2)
    private int code;

    @ShadowField(3)
    private String errorMsg;

    @ShadowField(4)
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

    @Override
    public String toString() {
        return "ShadowRPCResponse{" +
                "traceId='" + traceId + '\'' +
                ", code=" + code +
                ", errorMsg='" + errorMsg + '\'' +
                ", result=" + result +
                '}';
    }
}
