package com.liubs.shadowrpc.proxy;

import com.liubs.shadowrpc.handler.ReceiveHolder;
import com.liubs.shadowrpc.init.ShadowClientsManager;
import com.liubs.shadowrpc.protocol.annotation.ShadowInterface;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequest;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;
import io.netty.channel.Channel;

import java.lang.reflect.Proxy;

/**
 * @author Liubsyy
 * @date 2023/12/3 11:29 PM
 **/
public class RemoteServerProxy {

    public static <T> T create(Channel channel, Class<T> sourceInterface,String serviceName) {

        ShadowInterface shadowInterface = sourceInterface.getAnnotation(ShadowInterface.class);
        if(null == shadowInterface) {
            throw new RuntimeException("服务未找到 @shadowInterface注解");
        }

        Object proxyInstance = Proxy.newProxyInstance(
                sourceInterface.getClassLoader(),
                new Class<?>[]{sourceInterface},

                (proxy, method, args) -> {
                    ShadowRPCRequest request = new ShadowRPCRequest();
                    request.setServiceName(serviceName);
                    request.setMethodName(method.getName());
                    request.setParamTypes(method.getParameterTypes());
                    request.setParams(args);
                    channel.writeAndFlush(request);

                    ShadowRPCResponse response = ReceiveHolder.getInstance().poll();
                    if(response != null) {
                        return response.getResult();
                    }else {
                        System.out.println("超时请求");
                        return null;
                    }
                }
        );

        return (T)proxyInstance;
    }

    public static <T> T create(Class<T> sourceInterface,String serviceName) {

        ShadowInterface shadowInterface = sourceInterface.getAnnotation(ShadowInterface.class);
        if(null == shadowInterface) {
            throw new RuntimeException("服务未找到 @shadowInterface注解");
        }

        Object proxyInstance = Proxy.newProxyInstance(
                sourceInterface.getClassLoader(),
                new Class<?>[]{sourceInterface},

                (proxy, method, args) -> {
                    ShadowRPCRequest request = new ShadowRPCRequest();
                    request.setServiceName(serviceName);
                    request.setMethodName(method.getName());
                    request.setParamTypes(method.getParameterTypes());
                    request.setParams(args);

                    Channel channel = ShadowClientsManager.getInstance().getBalanceShadowClient().getChannel();
                    channel.writeAndFlush(request);

                    ShadowRPCResponse response = ReceiveHolder.getInstance().poll();
                    if(response != null) {
                        System.out.println("请求响应"+response.getResult());
                        return response.getResult();
                    }else {
                        System.out.println("超时请求");
                        return null;
                    }
                }
        );

        return (T)proxyInstance;
    }

}
