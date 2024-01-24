package com.liubs.shadowrpc.registry.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liubs.shadowrpc.base.util.JsonUtil;

/**
 * 服务节点
 * @author Liubsyy
 * @date 2023/12/31
 **/
public class ServerNode {
    private String group;
    private String ip;
    private int port;

    //这个无参构造函数给json用
    public ServerNode() {
    }

    public ServerNode(String group, String ip, int port) {
        this.group = group;
        this.ip = ip;
        this.port = port;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public String toString() {
        return "ServerNode{" +
                "group='" + group + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    public byte[] toBytes(){

        try {
            return JsonUtil.serialize(this).getBytes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public static ServerNode buildInstance(byte[] bytes) {
        try {
            return JsonUtil.deserialize(new String(bytes), ServerNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
