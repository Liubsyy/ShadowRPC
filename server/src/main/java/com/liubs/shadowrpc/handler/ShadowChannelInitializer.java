package com.liubs.shadowrpc.handler;


import com.liubs.shadowrpc.config.ShadowServerConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author Liubsyy
 * @date 2023/12/4 11:45 PM
 **/
public class ShadowChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();


        //qps请求量统计
        if(ShadowServerConfig.getInstance().isQpsStat()) {
            pipeline.addLast(new QpsStatHandler());
        }

        //处理帧边界，解决拆包和粘包问题
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,
                0, 4, 0, 4));

        //消息序列化和反序列化
        pipeline.addLast(new MessageHandler());

        ServerHandler serverHandler = new ServerHandler();
        pipeline.addLast(serverHandler);
    }
}
