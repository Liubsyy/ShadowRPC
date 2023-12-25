package com.liubs.shadowrpc.protocol.model;


/**
 * 抽象出来的request实体，任何协议请求格式都转化成这个model然后处理逻辑
 * @author Liubsyy
 * @date 2023/12/25
 */
public class RequestModel {

    private String traceId;

    private String serviceName;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] params;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
