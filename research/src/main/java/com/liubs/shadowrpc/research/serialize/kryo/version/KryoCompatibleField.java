package com.liubs.shadowrpc.research.serialize.kryo.version;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.google.gson.Gson;
import com.liubs.shadowrpc.research.entity.DynamicMan;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * 使用CompatibleFieldSerializer，增加字段时兼容处理
 * CompatibleFieldSerializer的原理：
 * 【序列化】先写入字段数量，再写入每个字段的名字，再写入具体的值
 * 【反序列化】先序列化字段名字出来一个字段数组，和现有结构字段比对，多的字段找不到则字段数组某个元素为空，跳过这个字段不反序列化
 *  如果字段数组比现有结构字段少的字段，因为没有反序列化默认也是null或者0
 *
 *  这里序列化字符串的方式是负数分隔符，比如写入一个字符串"ace", 先写入'a',再写入'c',写入'e'的时候直接存负数 'e' | 0x80, 下次直接读取到负数为止作为一个字符串的分隔
 *
 *  【缺点】每个字段名字都写入了序列化数组，时间和空间都会成比例冗余
 *
 * @author Liubsyy
 * @date 2023/12/2 6:47 PM
 */
public class KryoCompatibleField {

    public static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);    //不需要提前注册
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        return kryo;
    });

    public static byte[] serialize(Object obj) throws Exception {
        Kryo kryo = kryoThreadLocal.get();

        ByteArrayOutputStream kryoBos = new ByteArrayOutputStream();
        Output output = new Output(kryoBos);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return kryoBos.toByteArray();
    }

    public static <T> T unSerialize(Class<T> classz, byte[] bytes) throws Exception {
        Kryo kryo = kryoThreadLocal.get();
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            return (T)kryo.readClassAndObject(input);
        }
    }


    public static void main(String[] args) throws Exception {
        //没有beat字段和有beat字段的序列化
        byte[] byteOrigin =   new byte[]{1, 0, 99, 111, 109, 46, 108, 105, 117, 98, 115, 46, 115, 104, 97, 100, 111, 119, 114, 112, 99, 46, 114, 101, 115, 101, 97, 114, 99, 104, 46, 101, 110, 116, 105, 116, 121, 46, 68, 121, 110, 97, 109, 105, 99, 77, 97, -18, 4, 97, 103, -27, 104, 101, 105, 103, 104, -12, 109, 111, 110, 101, -7, 110, 97, 109, -27, 2, 44, 9, -44, 2, 10, 0, 0, 0, 0, 0, -120, -61, 64, 3, 84, 111, -19};
        byte[] byteAddField = new byte[]{1, 0, 99, 111, 109, 46, 108, 105, 117, 98, 115, 46, 115, 104, 97, 100, 111, 119, 114, 112, 99, 46, 114, 101, 115, 101, 97, 114, 99, 104, 46, 101, 110, 116, 105, 116, 121, 46, 68, 121, 110, 97, 109, 105, 99, 77, 97, -18, 5, 97, 103, -27, 98, 101, 97, -12, 104, 101, 105, 103, 104, -12, 109, 111, 110, 101, -7, 110, 97, 109, -27, 2, 44, 2, -24, 7, 9, -44, 2, 10, 0, 0, 0, 0, 0, -120, -61, 64, 3, 84, 111, -19};

        DynamicMan dynamicMan = new DynamicMan();
        dynamicMan.setName("Tom");
        dynamicMan.setAge(22);
        dynamicMan.setHeight(170L);
        dynamicMan.setMoney(10000D);

        byte[] bytes = serialize(dynamicMan);
        System.out.println("序列化: size="+bytes.length);
        System.out.println(Arrays.toString(bytes));

        DynamicMan unSerializeMan = unSerialize(DynamicMan.class, bytes);
        System.out.println("反序列化: "+new Gson().toJson(unSerializeMan));
    }
}
