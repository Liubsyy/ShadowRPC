package com.liubs.shadowrpc.research.serialize.strategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.liubs.shadowrpc.research.entity.Person;
import com.liubs.shadowrpc.research.entity.SimplePerson;
import com.liubs.shadowrpc.research.entity.User;
import com.liubs.shadowrpc.research.entity.Worker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Liubsyy
 * @date 2023/12/2 10:19 AM
 */
public class KryoSerialize implements ISerialize {

    public static ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);    //不需要提前注册
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
