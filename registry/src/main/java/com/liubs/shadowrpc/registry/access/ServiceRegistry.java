package com.liubs.shadowrpc.registry.access;

import com.liubs.shadowrpc.registry.constant.ServiceRegistryPath;
import com.liubs.shadowrpc.registry.entity.ServerNode;
import com.liubs.shadowrpc.registry.zk.ZooKeeperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务注册器
 * @author Liubsyy
 * @date 2023/12/30
 **/
public class ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private String registryPath;
    private String zkNodePath;
    private ZooKeeperClient zooKeeperClient;

    public ServiceRegistry(String registryPath) {
        this.registryPath = registryPath;
        this.zooKeeperClient = new ZooKeeperClient(registryPath);
    }


    public void registerServer(ServerNode serverNode) {
        try {
            String path = ServiceRegistryPath.getServerNodePath(serverNode.getGroup(),
                    ServiceRegistryPath.uniqueKey(serverNode.getIp(),serverNode.getPort()));
            this.zkNodePath = zooKeeperClient.create(path, serverNode.toBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegisterSever() {
        //删除注册的服务
        try {
            if(null != zkNodePath) {
                zooKeeperClient.delete(zkNodePath);
            }
        } catch (Exception e) {
            logger.error("Remove registry node err ",e);
        }

        zooKeeperClient.close();
    }

}
