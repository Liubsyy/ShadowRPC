package com.liubs.shadowrpc.server.service;

import com.liubs.shadowrpc.base.config.ServerConfig;
import com.liubs.shadowrpc.base.module.ModulePool;
import com.liubs.shadowrpc.protocol.SerializeModule;
import com.liubs.shadowrpc.server.ServerModule;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:49 PM
 */
public class ServerManager {
    private static final Logger logger = LoggerFactory.getLogger(ServerManager.class);

    private ServerConfig serverConfig;

    private Server server;


    //序列化
    private SerializeModule serializeModule = ModulePool.getModule(SerializeModule.class);

    //服务
    private ServerModule serverModule = ModulePool.getModule(ServerModule.class);

    private List<String> packageNames;


    public List<String> getPackageNames() {
        return packageNames;
    }


    public void setPackageNames(List<String> packageNames) {
        this.packageNames = packageNames;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public SerializeModule getSerializeModule() {
        return serializeModule;
    }

    public void setSerializeModule(SerializeModule serializeModule) {
        this.serializeModule = serializeModule;
    }

    public ServerModule getServerModule() {
        return serverModule;
    }

    public void setServerModule(ServerModule serverModule) {
        this.serverModule = serverModule;
    }

    public ServerManager() {
    }


    public Server start(){

        serverConfig.checkValid();

        //序列化模块初始化
        serializeModule.init(serverConfig,packageNames);

        //服务初始化
        serverModule.init(serverConfig,packageNames);

        //启动服务
        server = new Server(serverConfig,serverConfig.getGroup(),serverConfig.getPort());
        if(!StringUtil.isNullOrEmpty(serverConfig.getRegistryUrl())) {
            server.setRegistry(serverConfig.getRegistryUrl());
        }
        server.start();

        return server;
    }



}
