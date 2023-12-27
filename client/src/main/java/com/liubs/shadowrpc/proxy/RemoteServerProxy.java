package com.liubs.shadowrpc.proxy;

import com.liubs.shadowrpc.handler.ReceiveHolder;
import com.liubs.shadowrpc.init.ShadowClientsManager;
import com.liubs.shadowrpc.protocol.annotation.ShadowInterface;
import com.liubs.shadowrpc.protocol.model.IModelParser;
import com.liubs.shadowrpc.protocol.model.RequestModel;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import io.netty.channel.Channel;

import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

                    if(method.getName().equals("toString")) {
                        System.out.println();
                    }

                    RequestModel requestModel = new RequestModel();
                    String traceId = UUID.randomUUID().toString();
                    requestModel.setTraceId(traceId);
                    requestModel.setServiceName(serviceName);
                    requestModel.setMethodName(method.getName());
                    requestModel.setParamTypes(method.getParameterTypes());
                    requestModel.setParams(args);

                    Future<?> future = ReceiveHolder.getInstance().initFuture(traceId);

                    IModelParser modelParser = SerializerManager.getInstance().getSerializer().getModelParser();
                    channel.writeAndFlush(modelParser.toRequest(requestModel));

                    ResponseModel responseModel = (ResponseModel)future.get(3, TimeUnit.SECONDS);
                    if(responseModel != null) {
                        return responseModel.getResult();
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

                    RequestModel requestModel = new RequestModel();
                    String traceId = UUID.randomUUID().toString();
                    requestModel.setTraceId(traceId);
                    requestModel.setServiceName(serviceName);
                    requestModel.setMethodName(method.getName());
                    requestModel.setParamTypes(method.getParameterTypes());
                    requestModel.setParams(args);

                    IModelParser modelParser = SerializerManager.getInstance().getSerializer().getModelParser();
                    Future<?> future = ReceiveHolder.getInstance().initFuture(traceId);

                    Channel channel = ShadowClientsManager.getInstance().getBalanceShadowClient().getChannel();
                    channel.writeAndFlush(modelParser.toRequest(requestModel));

                    channel.writeAndFlush(modelParser.toRequest(requestModel));

                    ResponseModel responseModel = (ResponseModel)future.get(3, TimeUnit.SECONDS);
                    if(responseModel != null) {
                        return responseModel.getResult();
                    }else {
                        System.out.println("超时请求");
                        return null;
                    }

                }
        );

        return (T)proxyInstance;
    }

}
