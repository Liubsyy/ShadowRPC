package com.liubs.shadowrpc.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.liubs.shadowrpc.protocol.constant.ResponseCode;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequest;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequestProto;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponseProto;
import com.liubs.shadowrpc.protocol.serializer.protobuf.ParserForType;
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

        Method targetMethod;
        if(msg instanceof ShadowRPCRequest) {
            ShadowRPCRequest request = (ShadowRPCRequest)msg;
            Object targetRPC = ServerManager.getInstance().getService(request.getServiceName());
            Class<?> classz = targetRPC.getClass();
            targetMethod = classz.getDeclaredMethod(request.getMethodName(), request.getParamTypes());
            targetMethod.setAccessible(true);

            Object result = targetMethod.invoke(targetRPC, request.getParams());
            ShadowRPCResponse response = new ShadowRPCResponse();
            response.setTraceId(request.getTraceId());
            response.setCode(ResponseCode.SUCCESS.getCode());
            response.setResult(result);

            // 响应客户端
            ctx.writeAndFlush(response);

        }else if(msg instanceof ShadowRPCRequestProto.ShadowRPCRequest) {
            ShadowRPCRequestProto.ShadowRPCRequest request = (ShadowRPCRequestProto.ShadowRPCRequest)msg;
            Object targetRPC = ServerManager.getInstance().getService(request.getServiceName());

            Class<?>[] paramTypes = new Class<?>[request.getParamTypesCount()];
            Object[] params = new Object[request.getParamsCount()];

            for(int i = 0,len=request.getParamsCount() ; i<len ;i++) {
                String serviceName = request.getParamTypes(i);
                ByteString bytes = request.getParams(i);

                MessageLite defaultInstance = ParserForType.getMessage(serviceName);
                Object paramObj;
                if(null == defaultInstance) {
                    //没有注册，容错处理，反射扫描
                    Class<?> aClass = Class.forName(serviceName);
                    Method parseFrom = aClass.getDeclaredMethod("parseFrom", ByteString.class);
                    paramObj = parseFrom.invoke(null, bytes);
                }else {
                    paramObj = defaultInstance.getParserForType().parseFrom(bytes);
                }

                paramTypes[i]  = paramObj.getClass();
                params[i]  = paramObj;
            }

            targetMethod = targetRPC.getClass().getDeclaredMethod(request.getMethodName(), paramTypes);
            targetMethod.setAccessible(true);

            MessageLite result = (MessageLite)targetMethod.invoke(targetRPC, params);
            ShadowRPCResponseProto.ShadowRPCResponse.Builder response = ShadowRPCResponseProto.ShadowRPCResponse.newBuilder();
            response.setTraceId(request.getTraceId());
            response.setCode(ResponseCode.SUCCESS.getCode());
            response.setResultClass(result.getClass().getName());
            response.setResult(result.toByteString());

            // 响应客户端
            ctx.writeAndFlush(response);

        }



        super.channelRead(ctx, msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
        cause.printStackTrace();
        ctx.close();
    }


}
