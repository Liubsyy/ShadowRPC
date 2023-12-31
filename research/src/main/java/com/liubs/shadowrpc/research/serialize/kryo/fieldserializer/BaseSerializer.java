package com.liubs.shadowrpc.research.serialize.kryo.fieldserializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.liubs.shadowrpc.research.entity.Person;
import com.liubs.shadowrpc.research.entity.VersionPerson;
import com.liubs.shadowrpc.research.serialize.strategy.ISerialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Liubsyy
 * @date 2023/12/3 12:04 AM
 **/
public class BaseSerializer implements ISerialize {
    protected Kryo kryo = new Kryo();

    public BaseSerializer(Class<? extends Serializer> serializer){
        if(null != serializer) {
            kryo.setDefaultSerializer(serializer);
        }
        kryo.register(Person.class);
        kryo.register(VersionPerson.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.HashMap.class);
    }


    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream kryoBos = new ByteArrayOutputStream();
        Output output = new Output(kryoBos);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return kryoBos.toByteArray();
    }

    @Override
    public <T> T unSerialize(Class<T> classz, byte[] bytes) throws Exception {
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            return (T)kryo.readClassAndObject(input);
        }
    }
}
