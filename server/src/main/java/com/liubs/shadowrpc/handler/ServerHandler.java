package com.liubs.shadowrpc.handler;


import com.liubs.shadowrpc.protocol.constant.ResponseCode;
import com.liubs.shadowrpc.protocol.model.IModelParser;
import com.liubs.shadowrpc.protocol.model.RequestModel;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import com.liubs.shadowrpc.service.ServerManager;
import com.liubs.shadowrpc.service.ServiceLookUp;
import com.liubs.shadowrpc.service.ServiceTarget;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Liubsyy
 * @date 2023/12/3 10:23 PM
 **/
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 假设msg已经是解码后的对象
//        System.out.println("Server received: " + msg);

        IModelParser modelParser = SerializerManager.getInstance().getSerializer().getModelParser();

        RequestModel requestModel = modelParser.fromRequest(msg);

        System.out.println("Server received: " + requestModel.getTraceId());

        executorService.execute(()->{
            try {

                ServiceLookUp serviceLookUp = new ServiceLookUp();
                serviceLookUp.setServiceName(requestModel.getServiceName());
                serviceLookUp.setMethodName(requestModel.getMethodName());
                serviceLookUp.setParamTypes(requestModel.getParamTypes());
                ServiceTarget targetRPC = ServerManager.getInstance().getRPC(serviceLookUp);

                Object result = targetRPC.invoke(requestModel.getParams());

                ResponseModel responseModel = new ResponseModel();
                responseModel.setTraceId(requestModel.getTraceId());
                responseModel.setCode(ResponseCode.SUCCESS.getCode());
                responseModel.setResult(result);

                // 响应客户端
                ctx.writeAndFlush(modelParser.toResponse(responseModel));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
        cause.printStackTrace();
        ctx.close();
    }


}
