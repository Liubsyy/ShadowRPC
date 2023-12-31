package com.liubs.shadowrpc.registry.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liubs.shadowrpc.base.util.JsonUtil;

/**
 * 服务节点
 * @author Liubsyy
 * @date 2023/12/31
 **/
public class ServerNode {
    private String ip;
    private int port;

    public ServerNode() {
    }

    public ServerNode(String ip, int port) {
        this.ip = ip;
        this.port = port;
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
                "ip='" + ip + '\'' +
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
