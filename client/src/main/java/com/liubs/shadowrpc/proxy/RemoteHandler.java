package com.liubs.shadowrpc.proxy;

import com.liubs.shadowrpc.handler.ReceiveHolder;
import com.liubs.shadowrpc.init.ShadowClient;
import com.liubs.shadowrpc.init.ShadowClientsManager;
import com.liubs.shadowrpc.protocol.model.IModelParser;
import com.liubs.shadowrpc.protocol.model.RequestModel;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(RemoteHandler.class);

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

        try{
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

            if(!channel.isOpen()) {
                logger.error("服务器已关闭,发送消息抛弃...");
                return null;
            }

            try{
                channel.writeAndFlush(modelParser.toRequest(requestModel)).sync();
            }catch (Exception e) {
                logger.error("发送请求{}失败",traceId);
                return null;
            }

            ResponseModel responseModel = (ResponseModel)future.get(3, TimeUnit.SECONDS);
            if(responseModel != null) {
                return responseModel.getResult();
            }else {
                ReceiveHolder.getInstance().deleteWait(traceId);
                logger.error("超时请求,抛弃消息{}",traceId);
                return null;
            }

        }catch (Throwable e) {
            logger.error("invoke err",e);
        }

        return null;
    }
}
