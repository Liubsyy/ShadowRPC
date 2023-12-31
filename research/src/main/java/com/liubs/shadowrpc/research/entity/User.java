package com.liubs.shadowrpc.research.entity;

import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2023/12/2 10:08 AM
 */
public class User implements Serializable {
    private String name;
    private String wechatPub;
    private String job;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWechatPub() {
        return wechatPub;
    }

    public void setWechatPub(String wechatPub) {
        this.wechatPub = wechatPub;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
