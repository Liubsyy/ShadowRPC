package com.liubs.shadowrpc.init;

import com.liubs.shadowrpc.registry.constant.ServiceRegistryConstant;
import com.liubs.shadowrpc.registry.zk.ZooKeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2023/12/18 11:41 PM
 **/
public class ShadowClientsManager {
    private static ShadowClientsManager instance = new ShadowClientsManager();

    private ZooKeeperClient zooKeeperClient;


    private List<ShadowClient> shadowClients = new ArrayList<>();

    public static ShadowClientsManager getInstance() {
        return instance;
    }


    public ShadowClientsManager connectZk(String zkUrl){
        zooKeeperClient = new ZooKeeperClient(zkUrl);


        //初始化状态会同步CHILD_ADDED事件，所以不用获取全量
        //监听增量变化事件
        zooKeeperClient.addChildrenListener(ServiceRegistryConstant.BASE_PATH, new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {

                String connectionUrl = new String(zooKeeperClient.read( event.getData().getPath()));
                String[] split = connectionUrl.split(":");


                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("Child added: " + event.getData().getPath());

                        ShadowClient shadowClient = new ShadowClient();
                        shadowClient.init(split[0],Integer.parseInt(split[1]));
                        shadowClients.add(shadowClient);

                        break;
                    case CHILD_REMOVED:
                        System.out.println("Child removed: " + event.getData().getPath());

                        Iterator<ShadowClient> iterator = shadowClients.iterator();
                        while(iterator.hasNext()) {
                            ShadowClient shadowClient1 = iterator.next();
                            if(shadowClient1.getConnectionUrl().equals(connectionUrl)) {
                                shadowClient1.close();
                                iterator.remove();
                            }
                        }


                        break;
                    case CHILD_UPDATED:
                        System.out.println("Child updated: " + event.getData().getPath());

                        break;
                }
            }
        });
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


}