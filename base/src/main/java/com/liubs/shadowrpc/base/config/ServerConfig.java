package com.liubs.shadowrpc.base.config;

/**
 * @author Liubsyy
 * @date 2024/1/15
 */
public class ServerConfig extends BaseConfig {


    //开放端口
    private int port = 2023;

    //注册中心，如果为空则为单点模式
    private String registryUrl;

    //qps统计开关
    private boolean qpsStat;



    public boolean isQpsStat() {
        return qpsStat;
    }

    public void setQpsStat(boolean qpsStat) {
        this.qpsStat = qpsStat;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRegistryUrl() {
        return registryUrl;
    }

    public void setRegistryUrl(String registryUrl) {
        this.registryUrl = registryUrl;
    }
}
