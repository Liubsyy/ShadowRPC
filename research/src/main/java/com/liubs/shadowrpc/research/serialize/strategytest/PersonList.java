package com.liubs.shadowrpc.research.serialize.strategytest;

import com.liubs.shadowrpc.research.entity.Person;
import com.liubs.shadowrpc.research.serialize.strategy.ISerialize;
import com.liubs.shadowrpc.research.util.TimeSizeStat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2023/12/2 12:01 PM
 */
public class PersonList {
    public static void main(String[] args) throws Exception {

        System.out.println("******** 预热 ********");
        PersonSerialize.runWithNum(1);


        System.out.println("******** 序列化List总时长比较 ********");
        TimeSizeStat timeSizeStat = TimeSizeStat.newInstance();
        List<Person> userList = new ArrayList<>();
        for(int i = 0;i<10000 ;i++) {
            Person userItem = Person.generatePerson();
            userList.add(userItem);
        }
        for(ISerialize serialize : ISerialize.SERIALIZES){
            timeSizeStat.begin();
            byte[] serializeBytes = serialize.serialize(userList);
            timeSizeStat.addSize(serializeBytes.length);
            long useNs = timeSizeStat.getUseTime();
            System.out.printf("[%s]序列化list累计用时: %d ns, %d ms,字节数:%d\n",serialize.getClass().getSimpleName(),useNs,useNs/1000000,timeSizeStat.getSize());
        }

    }
}
