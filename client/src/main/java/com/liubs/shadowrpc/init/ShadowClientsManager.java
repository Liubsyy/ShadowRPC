package com.liubs.shadowrpc.init;

import com.liubs.shadowrpc.registry.access.ServiceDiscovery;
import com.liubs.shadowrpc.registry.constant.ServerChangeType;
import com.liubs.shadowrpc.registry.entity.ServerNode;
import com.liubs.shadowrpc.registry.listener.ServiceListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2023/12/18 11:41 PM
 **/
public class ShadowClientsManager {
    private static ShadowClientsManager instance = new ShadowClientsManager();

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();;

    private ServiceDiscovery serviceDiscovery;
    //private ZooKeeperClient zooKeeperClient;


    private List<ShadowClient> shadowClients = new ArrayList<>();

    public static ShadowClientsManager getInstance() {
        return instance;
    }


    public ShadowClientsManager connectRegistry(String zkUrl){
        //zooKeeperClient = new ZooKeeperClient(zkUrl);
        serviceDiscovery = new ServiceDiscovery(zkUrl);

        //监听增量变化事件
        //初始化状态会同步SERVER_ADDED事件，所以不用获取全量
        serviceDiscovery.watchService(new ServiceListener() {
            @Override
            public void onServerChange(ServerChangeType changeType, ServerNode serverNode) {
                if(changeType == ServerChangeType.SERVER_ADDED) {
                    System.out.println("Child added: " + serverNode);

                    ShadowClient shadowClient = new ShadowClient(eventLoopGroup);
                    shadowClient.init(serverNode.getIp(),serverNode.getPort());
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

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        return this;
    }


    private static int count = 0;
    public ShadowClient getBalanceShadowClient(){
        ShadowClient shadowClient = shadowClients.get(count % shadowClients.size());
        count++;
        return shadowClient;
    }

    public List<ShadowClient> getShadowClients(){
        return shadowClients;
    }

    public void stop(){
        try{
            shadowClients.forEach(ShadowClient::close);

            serviceDiscovery.close();

        } finally {

        }
    }

}