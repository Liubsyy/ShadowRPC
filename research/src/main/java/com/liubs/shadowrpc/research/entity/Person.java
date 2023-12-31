package com.liubs.shadowrpc.research.entity;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

import java.io.Serializable;
import java.util.Random;

/**
 * @author Liubsyy
 * @date 2023/11/30 5:56 PM
 */

public class Person implements Serializable {

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

    public static Person generatePerson(){
        Random random = new Random();
        Person person = new Person();

        person.setSex((byte)random.nextInt(2));
        person.setLike((short)random.nextInt(100));
        person.setHair('h');
        person.setMan(person.getSex()==1);

        person.setName(Generator.generateName());
        person.setAge(10+random.nextInt(30));
        person.setHeight(150+random.nextInt(30));
        person.setWeight(60+random.nextInt(20));
        person.setMoney(10000+random.nextInt(10000));

        return person;
    }
}
