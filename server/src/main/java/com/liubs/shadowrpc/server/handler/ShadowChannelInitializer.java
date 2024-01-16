package com.liubs.shadowrpc.server.handler;


import com.liubs.shadowrpc.base.config.ServerConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author Liubsyy
 * @date 2023/12/4 11:45 PM
 **/
public class ShadowChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ServerConfig serverConfig;

    public ShadowChannelInitializer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();


        //qps请求量统计
        if(serverConfig.isQpsStat()) {
            pipeline.addLast(new QpsStatHandler());
        }

        //处理帧边界，解决拆包和粘包问题
        pipeline.addLast(new LengthFieldBasedFrameDecoder(serverConfig.getMaxFrameLength(),
                0, 4, 0, 4));

        //消息序列化和反序列化
        pipeline.addLast(new MessageHandler());

        ServerHandler serverHandler = new ServerHandler();
        pipeline.addLast(serverHandler);
    }
}
