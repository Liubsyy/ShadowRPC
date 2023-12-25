package com.liubs.shadowrpc.handler;


import com.liubs.shadowrpc.protocol.constant.ResponseCode;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;
import com.liubs.shadowrpc.protocol.model.IModelParser;
import com.liubs.shadowrpc.protocol.model.RequestModel;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import com.liubs.shadowrpc.service.ServerManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

/**
 * @author Liubsyy
 * @date 2023/12/3 10:23 PM
 **/
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 假设msg已经是解码后的对象
        System.out.println("Server received: " + msg);

        IModelParser modelParser = SerializerManager.getInstance().getSerializer().getModelParser();

        RequestModel requestModel = modelParser.fromRequest(msg);
        Object targetRPC = ServerManager.getInstance().getService(requestModel.getServiceName());
        Class<?> classz = targetRPC.getClass();
        Method targetMethod = classz.getDeclaredMethod(requestModel.getMethodName(), requestModel.getParamTypes());
        targetMethod.setAccessible(true);

        Object result = targetMethod.invoke(targetRPC, requestModel.getParams());

        ResponseModel responseModel = new ResponseModel();
        responseModel.setTraceId(requestModel.getTraceId());
        responseModel.setCode(ResponseCode.SUCCESS.getCode());
        responseModel.setResult(result);

        // 响应客户端
        ctx.writeAndFlush(modelParser.toResponse(responseModel));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
        cause.printStackTrace();
        ctx.close();
    }


}
