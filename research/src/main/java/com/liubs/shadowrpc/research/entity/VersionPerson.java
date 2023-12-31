package com.liubs.shadowrpc.research.entity;



import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;

import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2023/12/3 12:47 AM
 **/

public class VersionPerson {

    @TaggedFieldSerializer.Tag(1)
    private byte sex;
    @TaggedFieldSerializer.Tag(2)
    private short like;
    @TaggedFieldSerializer.Tag(3)
    private char hair;
    @TaggedFieldSerializer.Tag(4)
    private boolean isMan;
    @TaggedFieldSerializer.Tag(5)
    private int age;
    @TaggedFieldSerializer.Tag(6)
    private double height;
    @TaggedFieldSerializer.Tag(7)
    private float weight;
    @TaggedFieldSerializer.Tag(8)
    private long money;
    @TaggedFieldSerializer.Tag(9)
    private String name;


    //不加这个注解的默认版本都是0
    @TaggedFieldSerializer.Tag(10)
    @VersionFieldSerializer.Since(1)
    private int addField1;

    @TaggedFieldSerializer.Tag(11)
    @VersionFieldSerializer.Since(2)
    private int addField2;


    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public short getLike() {
        return like;
    }

    public void setLike(short like) {
        this.like = like;
    }

    public char getHair() {
        return hair;
    }

    public void setHair(char hair) {
        this.hair = hair;
    }

    public boolean isMan() {
        return isMan;
    }

    public void setMan(boolean man) {
        isMan = man;
    }

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

    public int getAddField1() {
        return addField1;
    }

    public void setAddField1(int addField1) {
        this.addField1 = addField1;
    }

    public int getAddField2() {
        return addField2;
    }

    public void setAddField2(int addField2) {
        this.addField2 = addField2;
    }
}
