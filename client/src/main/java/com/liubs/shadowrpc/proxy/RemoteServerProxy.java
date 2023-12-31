package com.liubs.shadowrpc.proxy;

import com.liubs.shadowrpc.init.ShadowClient;
import com.liubs.shadowrpc.protocol.annotation.ShadowInterface;

import java.lang.reflect.Proxy;

/**
 * @author Liubsyy
 * @date 2023/12/3 11:29 PM
 **/
public class RemoteServerProxy {

    public static <T> T create(ShadowClient shadowClient, Class<T> serviceStub, String serviceName) {

        ShadowInterface shadowInterface = serviceStub.getAnnotation(ShadowInterface.class);
        if(null == shadowInterface) {
            throw new RuntimeException("服务未找到 @shadowInterface注解");
        }

        Object proxyInstance = Proxy.newProxyInstance(
                serviceStub.getClassLoader(),
                new Class<?>[]{serviceStub},
                new RemoteHandler(shadowClient,serviceStub,serviceName)
        );

        return (T)proxyInstance;
    }

    public static <T> T create(Class<T> serviceStub,String serviceName) {

        ShadowInterface shadowInterface = serviceStub.getAnnotation(ShadowInterface.class);
        if(null == shadowInterface) {
            throw new RuntimeException("服务未找到 @shadowInterface注解");
        }

        Object proxyInstance = Proxy.newProxyInstance(
                serviceStub.getClassLoader(),
                new Class<?>[]{serviceStub},
                new RemoteHandler(serviceStub,serviceName)
        );

        return (T)proxyInstance;
    }

}
