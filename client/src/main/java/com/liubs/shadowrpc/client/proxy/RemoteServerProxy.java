package com.liubs.shadowrpc.client.proxy;

import com.liubs.shadowrpc.base.annotation.ShadowInterface;
import com.liubs.shadowrpc.client.connection.IConnection;

import java.lang.reflect.Proxy;

/**
 * @author Liubsyy
 * @date 2023/12/3 11:29 PM
 **/
public class RemoteServerProxy {

    public static <T> T create(IConnection connection, Class<T> serviceStub, String serviceName) {

        ShadowInterface shadowInterface = serviceStub.getAnnotation(ShadowInterface.class);
        if(null == shadowInterface) {
            throw new RuntimeException("服务未找到 @shadowInterface注解");
        }

        Object proxyInstance = Proxy.newProxyInstance(
                serviceStub.getClassLoader(),
                new Class<?>[]{serviceStub},
                new RemoteHandler(connection,serviceStub,serviceName)
        );

        return (T)proxyInstance;
    }

}
