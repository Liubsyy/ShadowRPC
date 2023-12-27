package com.liubs.shadowrpc.handler;

import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;
import com.liubs.shadowrpc.protocol.model.IModelParser;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Liubsyy
 * @date 2023/12/3 10:29 PM
 **/


public class ClientHandler extends ChannelInboundHandlerAdapter{

        @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 当连接激活时，可以发送一条消息
//        System.out.println("Client connected.");
//        ctx.writeAndFlush("Hello, Server!");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IModelParser modelParser = SerializerManager.getInstance().getSerializer().getModelParser();
        ResponseModel responseModel = modelParser.fromResponse(msg);
        ReceiveHolder.getInstance().receiveData(responseModel);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 在发生异常时执行
        cause.printStackTrace();
        ctx.close();
    }



}

