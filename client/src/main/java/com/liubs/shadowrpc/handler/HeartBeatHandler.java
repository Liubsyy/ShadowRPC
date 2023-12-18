package com.liubs.shadowrpc.handler;

import com.liubs.shadowrpc.protocol.entity.HeartBeatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;


/**
 * @author Liubsyy
 * @date 2023/12/15 9:35 PM
 **/

public class HeartBeatHandler extends IdleStateHandler {

    public HeartBeatHandler() {
        super(0, 0, 3);
    }


    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        ctx.writeAndFlush(HeartBeatMessage.SINGLETON);
    }
}
