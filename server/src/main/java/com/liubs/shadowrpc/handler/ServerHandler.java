package com.liubs.shadowrpc.handler;

import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequest;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;
import com.liubs.shadowrpc.service.ServerManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
/**
 * @author Liubsyy
 * @date 2023/12/3 10:23 PM
 **/
public class ServerHandler extends SimpleChannelInboundHandler<ShadowRPCRequest> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ShadowRPCRequest request) {
        // 假设msg已经是解码后的对象
        System.out.println("Server received: " + request);

        try {
            Object targetRPC = ServerManager.getInstance().getService(request.getServiceName());
            Class<?> classz = targetRPC.getClass();
            Method declaredMethod = classz.getDeclaredMethod(request.getMethodName(), request.getParamTypes());
            declaredMethod.setAccessible(true);

            Object result = declaredMethod.invoke(targetRPC, request.getParams());


            ShadowRPCResponse response = new ShadowRPCResponse();
            response.setTraceId(request.getTraceId());
            response.setSuccess(true);
            response.setResult(result);

            // 响应客户端
            ctx.writeAndFlush(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
        cause.printStackTrace();
        ctx.close();
    }


}
