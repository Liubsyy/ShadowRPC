package com.liubs.shadowrpc.server.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Liubsyy
 * @date 2023/12/28
 **/
public class ServiceTarget {
    private Object targetObj;
    private Method method;

    public Object getTargetObj() {
        return targetObj;
    }

    public void setTargetObj(Object targetObj) {
        this.targetObj = targetObj;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object invoke(Object [] params) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(targetObj,params);
    }
}
