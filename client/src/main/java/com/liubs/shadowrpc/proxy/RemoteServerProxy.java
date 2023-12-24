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
import com.liubs.shadowrpc.protocol.serializer.SerializerEnum;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import com.liubs.shadowrpc.protocol.serializer.protobuf.ParserForType;
import io.netty.channel.Channel;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
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

                    if(SerializerManager.getInstance().getSerializer() == SerializerEnum.PROTOBUF) {
                        ShadowRPCRequestProto.ShadowRPCRequest request = ShadowRPCRequestProto.ShadowRPCRequest.newBuilder()
                                .setServiceName(serviceName)
                                .setMethodName(method.getName())
                                .addAllParamTypes(Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.toList()))
                                .addAllParams(Arrays.stream(args).map(c->((MessageLite)c).toByteString()).collect(Collectors.toList())).build();

                        channel.writeAndFlush(request);

                        ShadowRPCResponseProto.ShadowRPCResponse response = (ShadowRPCResponseProto.ShadowRPCResponse)ReceiveHolder.getInstance().poll();
                        if(response != null) {
                            String resultClass = response.getResultClass();
                            ByteString resultBytes = response.getResult();


                            MessageLite defaultInstance = ParserForType.getMessage(resultClass);
                            Object resultObj;
                            if(null == defaultInstance) {
                                //没有注册，容错处理，反射扫描
                                Class<?> aClass = Class.forName(resultClass);
                                Method parseFrom = aClass.getDeclaredMethod("parseFrom", ByteString.class);
                                resultObj = parseFrom.invoke(null, resultBytes);
                            }else {
                                resultObj = defaultInstance.getParserForType().parseFrom(resultBytes);
                            }
                            return resultObj;
                        }else {
                            System.out.println("超时请求");
                            return null;
                        }
                    }else {
                        ShadowRPCRequest request = new ShadowRPCRequest();
                        request.setServiceName(serviceName);
                        request.setMethodName(method.getName());
                        request.setParamTypes(method.getParameterTypes());
                        request.setParams(args);
                        channel.writeAndFlush(request);

                        ShadowRPCResponse response = (ShadowRPCResponse)ReceiveHolder.getInstance().poll();
                        if(response != null) {
                            return response.getResult();
                        }else {
                            System.out.println("超时请求");
                            return null;
                        }
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
