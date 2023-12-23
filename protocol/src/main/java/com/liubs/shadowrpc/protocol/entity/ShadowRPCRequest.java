package com.liubs.shadowrpc.protocol.entity;

import com.liubs.shadowrpc.protocol.annotation.ShadowEntity;
import com.liubs.shadowrpc.protocol.annotation.ShadowField;

import java.util.Arrays;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:38 PM
 **/
@ShadowEntity
public class ShadowRPCRequest {

    @ShadowField(1)
    private String traceId;

    @ShadowField(2)
    private String serviceName;

    @ShadowField(3)
    private String methodName;

    @ShadowField(4)
    private Class<?>[] paramTypes;

    @ShadowField(5)
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

    @Override
    public String toString() {
        return "ShadowRPCRequest{" +
                "traceId='" + traceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
