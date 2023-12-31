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
 * @author Liubsyy
 * @date 2023/12/4 12:30 AM
 **/
public class KryoSerializeCompatibleRegister implements ISerialize{
    public static ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);

        kryo.register(User.class);
        kryo.register(Person.class);
        kryo.register(Worker.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.HashMap.class);
        kryo.register(SimplePerson.class);
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
