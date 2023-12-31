package com.liubs.shadowrpc.research.serialize.strategytest;

import com.liubs.shadowrpc.research.entity.Generator;
import com.liubs.shadowrpc.research.entity.SimplePerson;
import com.liubs.shadowrpc.research.entity.SimplePersonOuterClass;
import com.liubs.shadowrpc.research.serialize.strategy.ISerialize;
import com.liubs.shadowrpc.research.util.TimeSizeStat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Liubsyy
 * @date 2023/12/2 2:38 PM
 */
public class SimplePersonBufSerialize {
    public static byte[] serialize(SimplePerson obj) throws Exception {
        SimplePersonOuterClass.SimplePerson person = SimplePersonOuterClass.SimplePerson.newBuilder()
                .setAge(obj.getAge())
                .setHeight(obj.getHeight())
                .setWeight(obj.getWeight())
                .setMoney(obj.getMoney())
                .setName(obj.getName())
                .build();
        return person.toByteArray();
    }

    public static <T> T unSerialize(byte[] bytes) throws Exception {
        return (T)SimplePersonOuterClass.SimplePerson.parseFrom(bytes);
    }


    public static void runWithNum(int n) throws Exception {

        //先初始化序列化的数据
        TimeSizeStat timeSizeStat = TimeSizeStat.newInstance();
        List<SimplePerson> userList = new ArrayList<>();
        Random random = new Random();
        for(int i = 0;i<n ;i++) {

            SimplePerson person = new SimplePerson();
            person.setName(Generator.generateName());
            person.setAge(10+random.nextInt(30));
            person.setHeight(150+random.nextInt(30));
            person.setWeight(60+random.nextInt(20));
            person.setMoney(10000+random.nextInt(10000));

            SimplePerson userItem = person;

            userList.add(userItem);
        }

        for(ISerialize serialize : ISerialize.SERIALIZES){
            timeSizeStat.begin();

            long sumOfSerializeTime = 0L;
            long sumOfUnSerializeTime = 0L;
            for(int i = 0;i<n ;i++) {
                long time1 = System.nanoTime();
                byte[] serializeBytes = serialize.serialize(userList.get(i));
                sumOfSerializeTime += System.nanoTime()-time1;

                timeSizeStat.addSize(serializeBytes.length);
                long time2 = System.nanoTime();
                SimplePerson simplePerson = serialize.unSerialize(SimplePerson.class, serializeBytes);
                sumOfUnSerializeTime += System.nanoTime()-time2;

            }
            System.out.printf("[%s]%d次累计,序列化用时: %20d ns, %d ms,     反序列化用时: %d ns, %d ms,字节数:%d\n",serialize.getClass().getSimpleName(),n
                    ,sumOfSerializeTime,sumOfSerializeTime/1000000
                    ,sumOfUnSerializeTime,sumOfUnSerializeTime/1000000,
                    timeSizeStat.getSize());
        }

        timeSizeStat.begin();
        long sumOfSerializeTime = 0L;
        long sumOfUnSerializeTime = 0L;
        for(int i = 0;i<n ;i++) {
            long time1 = System.nanoTime();
            byte[] serializeBytes = serialize(userList.get(i));
            sumOfSerializeTime += System.nanoTime()-time1;

            timeSizeStat.addSize(serializeBytes.length);

            long time2 = System.nanoTime();
            SimplePersonOuterClass.SimplePerson simplePerson = unSerialize(serializeBytes);
            sumOfUnSerializeTime += System.nanoTime()-time2;
        }
        System.out.printf("[%s]%d次累计,序列化用时: %20d ns, %d ms,     反序列化用时: %d ns, %d ms,字节数:%d\n",PersonProtoBufSerialize.class.getSimpleName(),n
                ,sumOfSerializeTime,sumOfSerializeTime/1000000
                ,sumOfUnSerializeTime,sumOfUnSerializeTime/1000000,
                timeSizeStat.getSize());

    }

    public static void main(String[] args) throws Exception {

        System.out.println("******** 预热 ********");
        runWithNum(1);


        System.out.println("******** 单对象累计100w次总时长比较 ********");
        runWithNum(1000000);

    }
}
