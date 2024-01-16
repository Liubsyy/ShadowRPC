package com.liubs.shadowrpc.client.init;

import com.liubs.shadowrpc.base.config.ClientConfig;
import com.liubs.shadowrpc.base.module.ModulePool;
import com.liubs.shadowrpc.client.ClientModule;
import com.liubs.shadowrpc.client.handler.ShadowChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 一个远程连接源，每一个远程服务器节点，即为一个ShadowClient实例
 * @author Liubsyy
 * @date 2023/12/18 10:56 PM
 **/

public class ShadowClient {

    private EventLoopGroup group;
    private Channel channel;

    private String remoteIp;
    private int remotePort;

    private ClientConfig config = ModulePool.getModule(ClientModule.class).getConfig();

    public ShadowClient() {
        group = new NioEventLoopGroup();
    }
    public ShadowClient(EventLoopGroup group) {
        this.group = group;
    }

    public void init(String host, int port){

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ShadowChannelInitializer(config));

            // 连接到服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();


            this.remoteIp = host;
            this.remotePort = port;
            System.out.printf("连接 %s:%d 成功\n",host,port);

            Runtime.getRuntime().addShutdownHook(new Thread(this::close));

        } catch (InterruptedException e) {
            e.printStackTrace();
            close();
        }
    }


    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public Channel getChannel() {
        return channel;
    }


    public void keep(){
        try{
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try{

            if(null != channel) {
                channel.close();
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
