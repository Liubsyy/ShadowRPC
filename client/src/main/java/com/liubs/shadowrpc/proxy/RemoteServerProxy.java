package com.liubs.shadowrpc.proxy;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.liubs.shadowrpc.handler.ReceiveHolder;
import com.liubs.shadowrpc.init.ShadowClientsManager;
import com.liubs.shadowrpc.protocol.annotation.ShadowInterface;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequest;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequestProto;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponseProto;
import com.liubs.shadowrpc.protocol.model.IModelParser;
import com.liubs.shadowrpc.protocol.model.RequestModel;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
import com.liubs.shadowrpc.protocol.serializer.SerializerEnum;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import com.liubs.shadowrpc.protocol.serializer.protobuf.ParserForType;
import io.netty.channel.Channel;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

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
                    requestModel.setTraceId(UUID.randomUUID().toString());
                    requestModel.setServiceName(serviceName);
                    requestModel.setMethodName(method.getName());
                    requestModel.setParamTypes(method.getParameterTypes());
                    requestModel.setParams(args);


                    IModelParser modelParser = SerializerManager.getInstance().getSerializer().getModelParser();
                    channel.writeAndFlush(modelParser.toRequest(requestModel));


                    Object response = ReceiveHolder.getInstance().poll();
                    if(null == response) {
                        return null;
                    }
                    ResponseModel responseModel = modelParser.fromResponse(response);
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
                    ShadowRPCRequest request = new ShadowRPCRequest();
                    request.setServiceName(serviceName);
                    request.setMethodName(method.getName());
                    request.setParamTypes(method.getParameterTypes());
                    request.setParams(args);

                    Channel channel = ShadowClientsManager.getInstance().getBalanceShadowClient().getChannel();
                    channel.writeAndFlush(request);

                    ShadowRPCResponse response = (ShadowRPCResponse)ReceiveHolder.getInstance().poll();
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
