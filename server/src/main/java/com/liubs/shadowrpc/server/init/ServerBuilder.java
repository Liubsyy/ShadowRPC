package com.liubs.shadowrpc.server.init;

import com.liubs.shadowrpc.base.config.ServerConfig;
import com.liubs.shadowrpc.server.service.ServerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2024/1/16
 */
public class ServerBuilder {

    private ServerConfig serverConfig;
    private List<String> packages;

    private ServerBuilder(){
        packages = new ArrayList<>();
    }

    public static ServerBuilder newBuilder(){
        return new ServerBuilder();
    }


    public ServerBuilder addPackage(String packageName) {
        this.packages.add(packageName);
        return this;
    }

    public ServerBuilder serverConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        return this;
    }

    public ServerManager build(){
        ServerManager serverManager = new ServerManager();
        serverManager.setServerConfig(serverConfig);
        serverManager.setPackageNames(packages);
        return serverManager;
    }
}
