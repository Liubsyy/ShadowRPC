package com.liubs.shadowrpc.server.init;

import com.liubs.shadowrpc.base.config.ServerConfig;

/**
 * @author Liubsyy
 * @date 2024/1/16
 */
public class ServerGroup {
    private ServerConfig serverConfig;
    private String[] packages;

    public ServerGroup(ServerConfig serverConfig, String[] packages) {
        this.serverConfig = serverConfig;
        this.packages = packages;
    }


}
