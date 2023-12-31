package com.liubs.shadowrpc.research.serialize.kryo.version;

import com.google.gson.Gson;
import com.liubs.shadowrpc.research.entity.VersionPerson;
import com.liubs.shadowrpc.research.serialize.kryo.fieldserializer.TaggedFieldTrunked;
import com.liubs.shadowrpc.research.serialize.strategy.ISerialize;

import java.util.Arrays;

/**
 * 使用KryoTaggedFieldSerializer，每个字段增加标签的方式序列化和反序列化
 * 增加字段，不管是序列化和反序列化，都能兼容
 *
 * @author Liubsyy
 * @date 2023/12/4 1:08 PM
 */
public class KryoTaggedField {

    public static void main(String[] args) throws Exception {
        //没有beat字段和有beat字段的序列化
        byte[] byteOrigin =     new byte[]{12, 10, 5, 1, 36, 0, 3, 2, 104, 0, 0, 6, 8, 0, 0, 0, 0, 0, 64, 101, 64, 0, 4, 1, 1, 0, 2, 2, 96, 0, 0, 8, 3, -96, -100, 1, 0, 9, 3, 84, 111, -19, 0, 1, 1, 1, 0, 7, 4, 0, 0, -126, 66, 0};
        byte[] byteAddField1 =  new byte[]{12, 11, 10, 2, -66, 1, 0, 5, 1, 36, 0, 3, 2, 104, 0, 0, 6, 8, 0, 0, 0, 0, 0, 64, 101, 64, 0, 4, 1, 1, 0, 2, 2, 96, 0, 0, 8, 3, -96, -100, 1, 0, 9, 3, 84, 111, -19, 0, 1, 1, 1, 0, 7, 4, 0, 0, -126, 66, 0};
        byte[] byteAddField2 =  new byte[]{12, 12, 10, 2, -66, 1, 0, 11, 2, -60, 1, 0, 5, 1, 36, 0, 3, 2, 104, 0, 0, 6, 8, 0, 0, 0, 0, 0, 64, 101, 64, 0, 4, 1, 1, 0, 2, 2, 96, 0, 0, 8, 3, -96, -100, 1, 0, 9, 3, 84, 111, -19, 0, 1, 1, 1, 0, 7, 4, 0, 0, -126, 66, 0};

        ISerialize serializer = new TaggedFieldTrunked();

        VersionPerson person = new VersionPerson();
        person.setSex((byte)1);
        person.setLike((short)96);
        person.setHair('h');
        person.setMan(person.getSex()==1);
        person.setName("Tom");
        person.setAge(18);
        person.setHeight(170);
        person.setWeight(65);
        person.setMoney(10000);

//        person.setAddField1(95);
//        person.setAddField2(98);

        byte[] bytes = serializer.serialize(person);
        System.out.println("序列化: size="+bytes.length);
        System.out.println(Arrays.toString(bytes));

        VersionPerson unSerializeMan = serializer.unSerialize(VersionPerson.class, byteOrigin);
        System.out.println("反序列化: "+new Gson().toJson(unSerializeMan));
    }
}
