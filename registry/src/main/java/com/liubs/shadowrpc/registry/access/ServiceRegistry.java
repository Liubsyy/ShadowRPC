package com.liubs.shadowrpc.registry.access;

import com.liubs.shadowrpc.registry.constant.ServiceRegistryConstant;
import com.liubs.shadowrpc.registry.entity.ServerNode;
import com.liubs.shadowrpc.registry.zk.ZooKeeperClient;

/**
 * 服务注册器
 * @author Liubsyy
 * @date 2023/12/30
 **/
public class ServiceRegistry {

    private String registryPath;
    private String zkNodePath;
    private ZooKeeperClient zooKeeperClient;

    public ServiceRegistry(String registryPath) {
        this.registryPath = registryPath;
        this.zooKeeperClient = new ZooKeeperClient(registryPath);
    }


    public void registerServer(ServerNode serverNode) {
        try {
            String path = ServiceRegistryConstant.getServerNodeStr(serverNode.getPort());
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
            e.printStackTrace();
        }

        zooKeeperClient.close();
    }

}
