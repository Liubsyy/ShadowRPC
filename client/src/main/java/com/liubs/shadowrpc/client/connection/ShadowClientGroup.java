package com.liubs.shadowrpc.client.connection;

import com.liubs.shadowrpc.client.loadbalance.LoadBalanceContext;
import com.liubs.shadowrpc.registry.access.ServiceDiscovery;
import com.liubs.shadowrpc.registry.constant.ServerChangeType;
import com.liubs.shadowrpc.registry.entity.ServerNode;
import com.liubs.shadowrpc.registry.listener.ServiceListener;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 一个ShadowClientGroup表示连接一个集群
 * @author Liubsyy
 * @date 2023/12/18 11:41 PM
 **/
public class ShadowClientGroup implements IConnection {
    private static final Logger logger = LoggerFactory.getLogger(ShadowClientGroup.class);


    private EventLoopGroup eventLoopGroup;

    private String registryUrl;

    //注册发现
    private ServiceDiscovery serviceDiscovery;

    //所有远程连接
    private List<ShadowClient> shadowClients;

    //负载均衡
    private LoadBalanceContext loadBalance;


    public ShadowClientGroup(String registryUrl) {
        this.eventLoopGroup = new NioEventLoopGroup();;
        this.registryUrl = registryUrl;
        this.shadowClients = new ArrayList<>();
        loadBalance = new LoadBalanceContext(this);
    }


    public List<ShadowClient> getShadowClients(){
        return shadowClients;
    }

    @Override
    public void init() {
        serviceDiscovery = new ServiceDiscovery(registryUrl);

        //监听增量变化事件
        //初始化状态会同步SERVER_ADDED事件，所以不用获取全量
        serviceDiscovery.watchService(new ServiceListener() {
            @Override
            public void onServerChange(ServerChangeType changeType, ServerNode serverNode) {
                if(changeType == ServerChangeType.SERVER_ADDED) {
                    System.out.println("Child added: " + serverNode);

                    ShadowClient shadowClient = new ShadowClient(serverNode.getIp(),serverNode.getPort(),eventLoopGroup);
                    shadowClient.init();
                    shadowClients.add(shadowClient);
                }else if(changeType == ServerChangeType.SERVER_REMOVED){
                    System.out.println("Child removed: " + serverNode);

                    Iterator<ShadowClient> iterator = shadowClients.iterator();
                    while(iterator.hasNext()) {
                        ShadowClient shadowClient1 = iterator.next();
                        if(serverNode.getIp().equals(shadowClient1.getRemoteIp()) && serverNode.getPort() == shadowClient1.getRemotePort()) {
                            shadowClient1.close();
                            iterator.remove();
                        }
                    }
                }else if(changeType == ServerChangeType.SERVER_UPDATED){
                    //TODO 更新节点的时候
                }
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public Channel getChannel() {
        return loadBalance.getBalanceShadowClient().getChannel();
    }

    @Override
    public void close() {
        try{
            shadowClients.forEach(ShadowClient::close);
            serviceDiscovery.close();
        } finally {
            logger.info("ShadowClientGroup closed...");
        }
    }
}