package com.liubs.shadowrpc.registry.access;

import com.liubs.shadowrpc.registry.constant.ServerChangeType;
import com.liubs.shadowrpc.registry.constant.ServiceRegistryPath;
import com.liubs.shadowrpc.registry.entity.ServerNode;
import com.liubs.shadowrpc.registry.listener.ServiceListener;
import com.liubs.shadowrpc.registry.zk.ZooKeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;


/**
 * 服务发现
 * @author Liubsyy
 * @date 2023/12/30
 **/
public class ServiceDiscovery {
    private String registryPath;
    private String zkNodePath;
    private ZooKeeperClient zooKeeperClient;

    public ServiceDiscovery(String registryPath) {
        this.registryPath = registryPath;
        this.zooKeeperClient = new ZooKeeperClient(registryPath);
    }


    public void watchService(String group,ServiceListener serviceListener) {
        zooKeeperClient.addChildrenListener(ServiceRegistryPath.getServerGroupPath(group), new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {

                ServerNode serverNode = null;
                switch (event.getType()) {
                    case CHILD_ADDED:
                        serverNode = ServerNode.buildInstance(event.getData().getData());
                        serviceListener.onServerChange(ServerChangeType.SERVER_ADDED,serverNode);
                        break;
                    case CHILD_REMOVED:
                        serverNode = ServerNode.buildInstance(event.getData().getData());
                        serviceListener.onServerChange(ServerChangeType.SERVER_REMOVED,serverNode);
                        break;
                    case CHILD_UPDATED:
                        serverNode = ServerNode.buildInstance(event.getData().getData());
                        serviceListener.onServerChange(ServerChangeType.SERVER_UPDATED,serverNode);
                        break;
                }
            }
        });
    }


    public void close(){
        if(null != zooKeeperClient) {
            zooKeeperClient.close();
        }
    }


}
