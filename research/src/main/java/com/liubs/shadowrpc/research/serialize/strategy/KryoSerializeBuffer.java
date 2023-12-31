package com.liubs.shadowrpc.research.serialize.strategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.ByteArrayInputStream;

import com.esotericsoftware.kryo.io.ByteBufferOutput;

/**
 * @author Liubsyy
 * @date 2023/12/2 11:30 AM
 */
public class KryoSerializeBuffer extends KryoSerialize {


    @Override
    public byte[] serialize(Object obj) throws Exception {
        Kryo kryo = kryoThreadLocal.get();
        try (Output output = new ByteBufferOutput(4096, -1)) {
            kryo.writeClassAndObject(output, obj);
            return output.toBytes();
        }
    }

    @Override
    public <T> T unSerialize(Class<T> classz, byte[] bytes) throws Exception {
        Kryo kryo = kryoThreadLocal.get();
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            return (T)kryo.readClassAndObject(input);
        }
    }

}
