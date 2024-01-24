package com.liubs.shadowrpc.client.proxy;

import com.liubs.shadowrpc.base.module.ModulePool;
import com.liubs.shadowrpc.client.connection.IConnection;
import com.liubs.shadowrpc.client.handler.ReceiveHolder;
import com.liubs.shadowrpc.protocol.SerializeModule;
import com.liubs.shadowrpc.protocol.model.IModelParser;
import com.liubs.shadowrpc.protocol.model.RequestModel;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
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
     * 如果不使用注册中心，则必须有ShadowClient
     */
    private IConnection clientConnection;

    /**
     * 远程接口stub
     */
    private  Class<?> serviceStub;


    /**
     * 集群
     */
    private String group;

    /**
     * 服务名
     */
    private String serviceName;


    private SerializeModule serializeModule = ModulePool.getModule(SerializeModule.class);

    public RemoteHandler(IConnection client, Class<?> serviceStub, String group,String serviceName) {
        this.clientConnection = client;
        this.serviceStub = serviceStub;
        this.group = group;
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

            IModelParser modelParser = serializeModule.getSerializer().getModelParser();
            Future<?> future = ReceiveHolder.getInstance().initFuture(traceId);

            Channel channel = clientConnection.getChannel(group);

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
