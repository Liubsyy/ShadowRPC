package com.liubs.shadowrpc.research.entity;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2023/12/2 2:37 PM
 */
public class SimplePerson implements Serializable {
    @TaggedFieldSerializer.Tag(1)
    private int age;
    @TaggedFieldSerializer.Tag(2)
    private double height;
    @TaggedFieldSerializer.Tag(3)
    private float weight;
    @TaggedFieldSerializer.Tag(4)
    private long money;
    @TaggedFieldSerializer.Tag(5)
    private String name;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
