package com.liubs.shadowrpc.server.service;

import com.liubs.shadowrpc.base.config.ServerConfig;
import com.liubs.shadowrpc.server.handler.ShadowChannelInitializer;
import com.liubs.shadowrpc.registry.access.ServiceRegistry;
import com.liubs.shadowrpc.registry.entity.ServerNode;
import com.liubs.shadowrpc.registry.util.IPUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Liubsyy
 * @date 2023/12/4 11:59 PM
 **/
public class Server {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;

    /**
     * 这个group主要是区分不同的集群，比如商品集群，订单集群，属于不同的group，在zk中注册不同的服务分组
     */
    private String group;
    private int port;

    private ServerConfig serverConfig;

    private ServiceRegistry serviceRegistry;

    public Server(ServerConfig serverConfig,String group, int port) {
        this.serverConfig = serverConfig;
        this.group = group;
        this.port = port;
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
    }

    public void start(){
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ShadowChannelInitializer(serverConfig))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            channelFuture = bootstrap.bind(port).sync();
            System.out.println("服务器启动成功...");

            //注册服务
            if(null != serviceRegistry) {
                serviceRegistry.registerServer(new ServerNode(group,IPUtil.getLocalIp(),port));
            }

            Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        } catch (InterruptedException e) {
            stop();
            throw new RuntimeException(e);
        }
    }



    public Server setRegistry(String zkUrl) {
        serviceRegistry = new ServiceRegistry(zkUrl);
        return this;
    }


    public void keep(){
        try{
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //优雅的退出
    public void stop(){
        if(null != serviceRegistry) {
            //清除注册服务
            serviceRegistry.unRegisterSever();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
