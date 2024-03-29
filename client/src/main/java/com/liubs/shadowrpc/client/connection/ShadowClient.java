package com.liubs.shadowrpc.client.connection;

import com.liubs.shadowrpc.base.config.ClientConfig;
import com.liubs.shadowrpc.base.module.ModulePool;
import com.liubs.shadowrpc.client.ClientModule;
import com.liubs.shadowrpc.client.handler.ShadowChannelInitializer;
import com.liubs.shadowrpc.client.proxy.RemoteServerProxy;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个远程连接源，每一个远程服务器节点，即为一个ShadowClient实例
 * @author Liubsyy
 * @date 2023/12/18 10:56 PM
 **/

public class ShadowClient implements IConnection{
    private static final Logger logger = LoggerFactory.getLogger(ShadowClientGroup.class);

    private EventLoopGroup group;
    private Channel channel;

    private String remoteIp;
    private int remotePort;

    private ClientConfig config = ModulePool.getModule(ClientModule.class).getConfig();

    public ShadowClient(String host, int port) {
        this(host,port,new NioEventLoopGroup());
    }
    public ShadowClient(String host, int port,EventLoopGroup group) {
        this.remoteIp = host;
        this.remotePort = port;
        this.group = group;
    }

    @Override
    public void init(){

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ShadowChannelInitializer(config));

            // 连接到服务器
            ChannelFuture future = bootstrap.connect(remoteIp, remotePort).sync();
            channel = future.channel();

            System.out.printf("连接 %s:%d 成功\n",remoteIp,remotePort);

            Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        } catch (InterruptedException e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public <T> T createRemoteProxy(Class<T> serviceStub, String service) {
        return RemoteServerProxy.create(this,serviceStub,service);
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

    @Override
    public Channel getChannel(String group) {
        return channel;
    }


    public void keep(){
        try{
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
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
