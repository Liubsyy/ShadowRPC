package com.liubs.shadowrpc.handler;

import com.liubs.shadowrpc.protocol.entity.HeartBeatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Liubsyy
 * @date 2023/12/15 9:35 PM
 **/

public class HeartBeatHandler extends IdleStateHandler {
    private static final Logger logger = LoggerFactory.getLogger(HeartBeatMessage.class);

    public HeartBeatHandler(int heartBeatWaitSeconds) {
        super(0, 0, 3);
    }


    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        logger.debug("发送心跳消息...");
        ctx.writeAndFlush(HeartBeatMessage.SINGLETON);
    }
}
