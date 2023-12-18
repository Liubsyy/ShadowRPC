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
    private boolean isSuccess;

    @ShadowField(3)
    private String error;

    @ShadowField(4)
    private Object result;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
