package com.liubs.shadowrpc.clientmini.connection;

import com.liubs.shadowrpc.base.annotation.ShadowInterface;
import com.liubs.shadowrpc.clientmini.exception.RemoteClosedException;
import com.liubs.shadowrpc.clientmini.exception.WriteTimeoutException;
import com.liubs.shadowrpc.clientmini.handler.ReceiveHolder;
import com.liubs.shadowrpc.clientmini.logger.Logger;
import com.liubs.shadowrpc.clientmini.nio.MessageSendFuture;
import com.liubs.shadowrpc.protocol.entity.JavaSerializeRPCRequest;
import com.liubs.shadowrpc.protocol.entity.JavaSerializeRPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Liubsyy
 * @date 2023/12/31
 **/
public class RemoteProxy implements InvocationHandler {
    private static final Logger logger = Logger.getLogger(RemoteProxy.class);


    private ShadowClient clientConnection;

    /**
     * 远程接口stub
     */
    private  Class<?> serviceStub;

    /**
     * 服务名
     */
    private String serviceName;

    private long writeChannelTimeout;

    public RemoteProxy(ShadowClient client, Class<?> serviceStub, String serviceName) {
        this.clientConnection = client;
        this.serviceStub = serviceStub;
        this.serviceName = serviceName;
        this.writeChannelTimeout = client.getNIOConfig().getWriteChannelTimeout();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        JavaSerializeRPCRequest requestModel = new JavaSerializeRPCRequest();
        String traceId = UUID.randomUUID().toString();
        requestModel.setTraceId(traceId);
        requestModel.setServiceName(serviceName);
        requestModel.setMethodName(method.getName());
        requestModel.setParamTypes(method.getParameterTypes());
        requestModel.setParams(args);


        Future<?> future = ReceiveHolder.getInstance().initFuture(traceId);

        if(!clientConnection.isRunning()) {
            throw new RemoteClosedException("服务器已经关闭，中断写入消息");
        }

        MessageSendFuture messageSendFuture = null;
        try{
            messageSendFuture = clientConnection.sendMessage(clientConnection.getRequestHandler().handleMessage(requestModel));
            if(null != messageSendFuture) {
                messageSendFuture.get(writeChannelTimeout,TimeUnit.MILLISECONDS);
            }
        }catch (Throwable e) {
            if(null != messageSendFuture) {
                messageSendFuture.cancel(true);
            }
            if(!clientConnection.isRunning()) {
                throw new RemoteClosedException("服务器已经关闭，中断发送消息");
            }
            throw new WriteTimeoutException(String.format("发送请求%s失败",traceId),e);
        }


        try{
            JavaSerializeRPCResponse responseModel = (JavaSerializeRPCResponse)future.get(3, TimeUnit.SECONDS);
            if(responseModel != null) {
                return responseModel.getResult();
            }else {
                logger.error("未获取到响应消息{}",traceId);
                return null;
            }
        }catch (TimeoutException timeoutException) {
            logger.error("超时请求,抛弃消息{}",traceId);
            ReceiveHolder.getInstance().deleteWait(traceId);
            return null;
        } catch (Exception e) {
            if(clientConnection.isRunning()) {
                throw e;
            }
            throw new RemoteClosedException("服务器已经关闭，写入消息失败");
        }
    }

    public static <T> T create(ShadowClient connection, Class<T> serviceStub, String serviceName) {

        ShadowInterface shadowInterface = serviceStub.getAnnotation(ShadowInterface.class);
        if(null == shadowInterface) {
            throw new RuntimeException("服务未找到 @shadowInterface注解");
        }

        Object proxyInstance = Proxy.newProxyInstance(
                serviceStub.getClassLoader(),
                new Class<?>[]{serviceStub},
                new RemoteProxy(connection,serviceStub,serviceName)
        );

        return (T)proxyInstance;
    }
}
