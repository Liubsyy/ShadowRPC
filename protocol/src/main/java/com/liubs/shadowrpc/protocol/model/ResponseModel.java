package com.liubs.shadowrpc.protocol.model;

/**
 * 抽象出来的response实体，任何协议请求格式都转化成这个model然后处理逻辑
 * @author Liubsyy
 * @date 2023/12/25
 */
public class ResponseModel {
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
