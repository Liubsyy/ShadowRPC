package com.liubs.shadowrpc.handler;

import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Liubsyy
 * @date 2023/12/3 10:29 PM
 **/


public class ClientHandler extends SimpleChannelInboundHandler<ShadowRPCResponse> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 当连接激活时，可以发送一条消息
//        System.out.println("Client connected.");
//        ctx.writeAndFlush("Hello, Server!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ShadowRPCResponse response) throws Exception {
        ReceiveHolder.getInstance().put(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 在发生异常时执行
        cause.printStackTrace();
        ctx.close();
    }



}

