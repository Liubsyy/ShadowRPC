package com.liubs.shadowrpc.base.config;

import com.liubs.shadowrpc.base.exception.ConfigFieldMissException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Liubsyy
 * @date 2024/1/15
 */
public class ServerConfig extends BaseConfig {

    //服务器集群群组，适用于集群模式
    private String group = "DefaultGroup";

    //开放端口
    private int port = 2023;

    //注册中心，如果为空则为单点模式
    private String registryUrl;

    //qps统计开关
    private boolean qpsStat;


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

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

    public boolean checkValid(){
        if(StringUtils.isEmpty(group)) {
            throw new ConfigFieldMissException("group未配置");
        }
        return true;
    }
}
