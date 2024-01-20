package com.liubs.shadowrpc.protocol.entity;

import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2024/1/20
 **/
public class JavaSerializeRPCRequest implements Serializable {


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
