package com.liubs.shadowrpc.research.serialize.kryo.version;

import com.google.gson.Gson;
import com.liubs.shadowrpc.research.entity.VersionPerson;
import com.liubs.shadowrpc.research.serialize.kryo.fieldserializer.VersionField;
import com.liubs.shadowrpc.research.serialize.strategy.ISerialize;

import java.util.Arrays;

/**
 *
 * 新增字段上加上注解VersionFieldSerializer.Since来区分版本
 * A->B，A序列化，B反序列化，序列化的时候写入A所有字段的最大版本号
 * 场景1: B增加字段，升级版本，A->B，B可以识别出哪个字段的版本大于Version(A),如果大于说明是新增的字段不反序列化
 * 场景2: A新增字段，升级版本，但是B版本比A低，感知不到新增版本字段，反序列化报错
 *
 * @author Liubsyy
 * @date 2023/12/3 12:44 AM
 **/
public class KryoVersionField {

    public static void main(String[] args) throws Exception {
        //没有beat字段和有beat字段的序列化
        byte[] byteOrigin =     new byte[]{12, 1, 36, 104, 0, 0, 0, 0, 0, 0, 64, 101, 64, 1, 96, 0, -96, -100, 1, 84, 111, -19, 1, 0, 0, -126, 66};
        byte[] byteAddField1 =  new byte[]{12, 2, -34, 1, 36, 104, 0, 0, 0, 0, 0, 0, 64, 101, 64, 1, 96, 0, -96, -100, 1, 84, 111, -19, 1, 0, 0, -126, 66};
        byte[] byteAddField2 =  new byte[]{12, 3, -34, 1, -68, 3, 36, 104, 0, 0, 0, 0, 0, 0, 64, 101, 64, 1, 96, 0, -96, -100, 1, 84, 111, -19, 1, 0, 0, -126, 66};

        ISerialize serializer = new VersionField();

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

        byte[] bytes = serializer.serialize(person);
        System.out.println("序列化: size="+bytes.length);
        System.out.println(Arrays.toString(bytes));

        VersionPerson unSerializeMan = serializer.unSerialize(VersionPerson.class, byteAddField1);
        System.out.println("反序列化: "+new Gson().toJson(unSerializeMan));
    }
}
