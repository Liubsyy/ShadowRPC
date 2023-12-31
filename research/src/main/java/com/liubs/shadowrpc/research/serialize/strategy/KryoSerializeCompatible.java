package com.liubs.shadowrpc.research.serialize.strategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.liubs.shadowrpc.research.entity.Person;
import com.liubs.shadowrpc.research.entity.SimplePerson;
import com.liubs.shadowrpc.research.entity.User;
import com.liubs.shadowrpc.research.entity.Worker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
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
 * @date 2023/12/2 6:52 PM
 */
public class KryoSerializeCompatible implements ISerialize{
    public static ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);    //不需要提前注册

        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) throws Exception {
        Kryo kryo = kryoThreadLocal.get();

        ByteArrayOutputStream kryoBos = new ByteArrayOutputStream();
        Output output = new Output(kryoBos);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return kryoBos.toByteArray();
    }

    @Override
    public <T> T unSerialize(Class<T> classz, byte[] bytes) throws Exception {
        Kryo kryo = kryoThreadLocal.get();
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            return (T)kryo.readClassAndObject(input);
        }
    }

}
