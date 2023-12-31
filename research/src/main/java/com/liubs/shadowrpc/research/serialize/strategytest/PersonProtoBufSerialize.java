package com.liubs.shadowrpc.research.serialize.strategytest;

import com.liubs.shadowrpc.research.entity.Person;
import com.liubs.shadowrpc.research.entity.PersonOuterClass;
import com.liubs.shadowrpc.research.serialize.strategy.ISerialize;
import com.liubs.shadowrpc.research.util.TimeSizeStat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2023/12/2 2:02 PM
 */
public class PersonProtoBufSerialize {

    public static byte[] serialize(Person obj) throws Exception {
        PersonOuterClass.Person person = PersonOuterClass.Person.newBuilder()
                .setSex(obj.getSex())
                .setLike(obj.getLike())
                .setHair(obj.getHair())
                .setIsMan(obj.isMan())
                .setAge(obj.getAge())
                .setHeight(obj.getHeight())
                .setWeight(obj.getWeight())
                .setMoney(obj.getMoney())
                .setName(obj.getName())
                .build();
        return person.toByteArray();
    }

    public <T> T unSerialize(byte[] bytes) throws Exception {
        return null;
    }


    public static void runWithNum(int n) throws Exception {

        //先初始化序列化的数据
        TimeSizeStat timeSizeStat = TimeSizeStat.newInstance();
        List<Person> userList = new ArrayList<>();
        for(int i = 0;i<n ;i++) {
            Person userItem = Person.generatePerson();

            userList.add(userItem);
        }

        for(ISerialize serialize : ISerialize.SERIALIZES){
            timeSizeStat.begin();
            for(int i = 0;i<n ;i++) {
                byte[] serializeBytes = serialize.serialize(userList.get(i));
                timeSizeStat.addSize(serializeBytes.length);
            }
            long useNs = timeSizeStat.getUseTime();
            System.out.printf("[%s]%d次累计用时: %d ns, %d ms,字节数:%d\n",serialize.getClass().getSimpleName(),n,useNs,useNs/1000000,timeSizeStat.getSize());
        }

        timeSizeStat.begin();
        for(int i = 0;i<n ;i++) {
            byte[] serializeBytes = serialize(userList.get(i));
            timeSizeStat.addSize(serializeBytes.length);
        }
        long useNs = timeSizeStat.getUseTime();
        System.out.printf("[%s]%d次累计用时: %d ns, %d ms,字节数:%d\n",PersonProtoBufSerialize.class.getSimpleName(),n,useNs,useNs/1000000,timeSizeStat.getSize());


    }

    public static void main(String[] args) throws Exception {

        System.out.println("******** 预热 ********");
        runWithNum(1);


        System.out.println("******** 单对象累计100w次总时长比较 ********");
        runWithNum(1000000);

    }

}
