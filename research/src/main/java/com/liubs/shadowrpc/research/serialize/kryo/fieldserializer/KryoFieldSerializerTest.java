package com.liubs.shadowrpc.research.serialize.kryo.fieldserializer;

import com.liubs.shadowrpc.research.entity.Generator;
import com.liubs.shadowrpc.research.entity.Person;
import com.liubs.shadowrpc.research.serialize.strategy.ISerialize;
import com.liubs.shadowrpc.research.util.TimeSizeStat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Liubsyy
 * @date 2023/12/3 12:17 AM
 **/
public class KryoFieldSerializerTest {

    //kryo各种序列化方式比较
    public static void main(String[] args) throws Exception {

        List<ISerialize> kryoSerializers = Arrays.asList(
                new DefaultField(),
                new CompatibleField(),
                new TaggedField(),
                new TaggedFieldTrunked(),
                new VersionField());


        System.out.println("******** 预热 ********");
        runWithNum(kryoSerializers,1);

        System.out.println("******** 单对象累计100w次总时长比较 ********");
        runWithNum(kryoSerializers,1000000);

    }
    

    public static void runWithNum(List<ISerialize> kryoSerializers,int n) throws Exception {

        //先初始化序列化的数据
        TimeSizeStat timeSizeStat = TimeSizeStat.newInstance();
        List<Person> userList = new ArrayList<>();
        Random random = new Random();
        for(int i = 0;i<n ;i++) {
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

            userList.add(person);
        }


        for(ISerialize serialize : kryoSerializers) {
            timeSizeStat.begin();
            long sumOfSerializeTime = 0L;
            long sumOfUnSerializeTime = 0L;
            for(int i = 0;i<n ;i++) {
                long time1 = System.nanoTime();
                byte[] serializeBytes = serialize.serialize(userList.get(i));
                sumOfSerializeTime += System.nanoTime()-time1;

                timeSizeStat.addSize(serializeBytes.length);
                long time2 = System.nanoTime();
                Person person = serialize.unSerialize(Person.class, serializeBytes);
                sumOfUnSerializeTime += System.nanoTime()-time2;

            }
            System.out.printf("[%s]%d次累计,序列化用时: %20d ns, %d ms,     反序列化用时: %d ns, %d ms,字节数:%d\n",serialize.getClass().getSimpleName(),n
                    ,sumOfSerializeTime,sumOfSerializeTime/1000000
                    ,sumOfUnSerializeTime,sumOfUnSerializeTime/1000000,
                    timeSizeStat.getSize());
        }


    }

}
