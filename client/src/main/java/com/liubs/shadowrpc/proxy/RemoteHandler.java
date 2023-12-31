package com.liubs.shadowrpc.proxy;

import com.liubs.shadowrpc.handler.ReceiveHolder;
import com.liubs.shadowrpc.init.ShadowClient;
import com.liubs.shadowrpc.init.ShadowClientsManager;
import com.liubs.shadowrpc.protocol.model.IModelParser;
import com.liubs.shadowrpc.protocol.model.RequestModel;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Liubsyy
 * @date 2023/12/31
 **/
public class RemoteHandler implements InvocationHandler {

    /**
     * 是否使用注册中心
     */
    private boolean useRegistry;

    /**
     * 如果不使用注册中心，则必须有ShadowClient
     */
    private ShadowClient client;

    /**
     * 远程接口stub
     */
    private  Class<?> serviceStub;

    /**
     * 服务名
     */
    private String serviceName;

    public RemoteHandler(ShadowClient client, Class<?> serviceStub, String serviceName) {
        this.useRegistry = false;
        this.client = client;
        this.serviceStub = serviceStub;
        this.serviceName = serviceName;
    }
    public RemoteHandler(Class<?> serviceStub, String serviceName) {
        this.useRegistry = true;
        this.serviceStub = serviceStub;
        this.serviceName = serviceName;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RequestModel requestModel = new RequestModel();
        String traceId = UUID.randomUUID().toString();
        requestModel.setTraceId(traceId);
        requestModel.setServiceName(serviceName);
        requestModel.setMethodName(method.getName());
        requestModel.setParamTypes(method.getParameterTypes());
        requestModel.setParams(args);

        IModelParser modelParser = SerializerManager.getInstance().getSerializer().getModelParser();
        Future<?> future = ReceiveHolder.getInstance().initFuture(traceId);

        Channel channel = null;
        if(useRegistry) {
            channel = ShadowClientsManager.getInstance().getBalanceShadowClient().getChannel();
        }else {
            channel = client.getChannel();
        }

        channel.writeAndFlush(modelParser.toRequest(requestModel));

        ResponseModel responseModel = (ResponseModel)future.get(3, TimeUnit.SECONDS);
        if(responseModel != null) {
            return responseModel.getResult();
        }else {
            System.out.println("超时请求");
            return null;
        }
    }
}
