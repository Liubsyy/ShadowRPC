package com.liubs.shadowrpc.research.entity;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2023/12/4 12:03 PM
 */
public class Request implements Serializable {

    @TaggedFieldSerializer.Tag(1)
    private String traceId;

    @TaggedFieldSerializer.Tag(2)
    private String className;

    @TaggedFieldSerializer.Tag(3)
    private String methodName;

    @TaggedFieldSerializer.Tag(4)
    private Class<?>[] paramTypes;

    @TaggedFieldSerializer.Tag(5)
    private Object[] params;


    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
