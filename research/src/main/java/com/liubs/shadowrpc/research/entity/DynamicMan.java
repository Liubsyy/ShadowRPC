package com.liubs.shadowrpc.research.entity;

import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2023/12/2 4:56 PM
 */
public class DynamicMan implements Serializable {

//    private int beat;
//    public int getBeat() {
//        return beat;
//    }
//
//    public void setBeat(int beat) {
//        this.beat = beat;
//    }

    private int age;
    private long height;
    private double money;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
