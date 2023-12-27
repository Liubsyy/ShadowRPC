package com.liubs.shadowrpc.service;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Liubsyy
 * @date 2023/12/28
 **/
public class ServiceLookUp {
    private String serviceName;
    private String methodName;
    private Object[] paramTypes;

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

    public Object[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Object[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceLookUp that = (ServiceLookUp) o;
        return Objects.equals(serviceName, that.serviceName) && Objects.equals(methodName, that.methodName) && Arrays.equals(paramTypes, that.paramTypes);
    }

    @Override
    public int hashCode() {
        //服务名，方法名，参数长度为一个hash
        int result = Objects.hash(serviceName, methodName, null == paramTypes ? 0 : paramTypes.length);
        return result;
    }
}
