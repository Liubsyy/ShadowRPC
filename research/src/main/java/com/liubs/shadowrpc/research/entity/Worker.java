package com.liubs.shadowrpc.research.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Liubsyy
 * @date 2023/11/30 6:00 PM
 */
public class Worker implements Serializable {
    private String workAddr;

    private Person info;

    private List<Person> friends;

    private Map<String,String> extendInfo;


    public String getWorkAddr() {
        return workAddr;
    }

    public void setWorkAddr(String workAddr) {
        this.workAddr = workAddr;
    }

    public Person getInfo() {
        return info;
    }

    public void setInfo(Person info) {
        this.info = info;
    }

    public List<Person> getFriends() {
        return friends;
    }

    public void setFriends(List<Person> friends) {
        this.friends = friends;
    }

    public Map<String, String> getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(Map<String, String> extendInfo) {
        this.extendInfo = extendInfo;
    }
}
