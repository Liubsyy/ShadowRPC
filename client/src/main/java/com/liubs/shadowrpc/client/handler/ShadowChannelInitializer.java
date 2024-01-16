package com.liubs.shadowrpc.client.handler;

import com.liubs.shadowrpc.base.config.ClientConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author Liubsyy
 * @date 2023/12/4 11:53 PM
 **/
public class ShadowChannelInitializer  extends ChannelInitializer<SocketChannel> {

    private ClientConfig config;

    public ShadowChannelInitializer(ClientConfig config) {
        this.config = config;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        //处理帧边界，解决拆包和粘包问题
        pipeline.addLast(new LengthFieldBasedFrameDecoder(config.getMaxFrameLength(),
                0, 4, 0, 4));

        //消息序列化和反序列化
        pipeline.addLast(new MessageHandler());

        //心跳机制
        if(config.isHeartBeat()) {
            pipeline.addLast(new HeartBeatHandler(config.getHeartBeatWaitSeconds()));
        }


        //处理消息的逻辑
        pipeline.addLast(new ClientHandler());

    }
}
