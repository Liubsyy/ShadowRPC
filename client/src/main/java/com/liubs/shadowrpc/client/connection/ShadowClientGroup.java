package com.liubs.shadowrpc.client.connection;

import com.liubs.shadowrpc.client.loadbalance.LoadBalanceContext;
import com.liubs.shadowrpc.client.proxy.RemoteServerProxy;
import com.liubs.shadowrpc.registry.access.ServiceDiscovery;
import com.liubs.shadowrpc.registry.constant.ServerChangeType;
import com.liubs.shadowrpc.registry.entity.ServerNode;
import com.liubs.shadowrpc.registry.listener.ServiceListener;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 集群模式
 * @author Liubsyy
 * @date 2023/12/18 11:41 PM
 **/
public class ShadowClientGroup implements IConnection {
    private static final Logger logger = LoggerFactory.getLogger(ShadowClientGroup.class);

    private EventLoopGroup eventLoopGroup;

    private String registryUrl;

    //注册发现
    private ServiceDiscovery serviceDiscovery;

    //group=>所有远程连接
    private Map<String,List<ShadowClient>> shadowClientsMap;

    //负载均衡
    private LoadBalanceContext loadBalance;


    public ShadowClientGroup(String registryUrl) {
        this.eventLoopGroup = new NioEventLoopGroup();;
        this.registryUrl = registryUrl;
        this.shadowClientsMap = new ConcurrentHashMap<>();
        loadBalance = new LoadBalanceContext(this);
    }

    public List<ShadowClient> getShadowClients(String group) {
        return shadowClientsMap.get(group);
    }

    @Override
    public void init() {
        serviceDiscovery = new ServiceDiscovery(registryUrl);

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public <T> T createRemoteProxy(final Class<T> serviceStub, String service) {

        if(!RemoteServerProxy.hasLoaded(service)) {
            String[] serviceArr = service.replace("shadowrpc://","").split("/");
            if(serviceArr.length < 2) {
                throw new IllegalArgumentException("service参数不符合规范");
            }
            String group = serviceArr[0];
            List<ShadowClient> shadowClients = shadowClientsMap.get(group);
            if(null == shadowClients) {
                synchronized (serviceStub) {
                    shadowClients =  shadowClientsMap.get(group);
                    if(null == shadowClients) {
                        shadowClients = new ArrayList<>();
                        final List<ShadowClient> finalShadowClients = shadowClients;
                        //监听增量变化事件
                        //初始化状态会同步SERVER_ADDED事件，所以不用获取全量
                        serviceDiscovery.watchService(group, (changeType, serverNode) -> {
                            if(changeType == ServerChangeType.SERVER_ADDED) {
                                System.out.println("Child added: " + serverNode);

                                ShadowClient shadowClient = new ShadowClient(serverNode.getIp(),serverNode.getPort(),eventLoopGroup);
                                shadowClient.init();
                                finalShadowClients.add(shadowClient);
                            }else if(changeType == ServerChangeType.SERVER_REMOVED){
                                System.out.println("Child removed: " + serverNode);

                                Iterator<ShadowClient> iterator = finalShadowClients.iterator();
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
                        });

                        shadowClientsMap.put(group,finalShadowClients);
                    }
                }
            }
        }

        return RemoteServerProxy.create(this,serviceStub,service);
    }

    @Override
    public Channel getChannel(String group) {
        return loadBalance.getBalanceShadowClient(group).getChannel(group);
    }

    @Override
    public void close() {
        try{
            shadowClientsMap.values().forEach(c-> c.forEach(ShadowClient::close));
            serviceDiscovery.close();
        } finally {
            logger.info("ShadowClientGroup closed...");
        }
    }
}