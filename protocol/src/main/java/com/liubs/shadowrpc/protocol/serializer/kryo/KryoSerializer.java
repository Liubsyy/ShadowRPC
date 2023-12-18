package com.liubs.shadowrpc.protocol.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequest;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;
import com.liubs.shadowrpc.protocol.serializer.ISerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:43 PM
 **/
public class KryoSerializer implements ISerializer {

    private static ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();

        kryo.setDefaultSerializer(new KryoFieldSerializerFactory());

        kryo.setReferences(false);
        kryo.setRegistrationRequired(false);    //不需要提前注册

        //注册一定会用到的，序列化可以省点空间
        kryo.register(Class.class);
        kryo.register(Class[].class);
        kryo.register(Object[].class);
        kryo.register(ShadowRPCRequest.class);
        kryo.register(ShadowRPCResponse.class);

        return kryo;
    });



    @Override
    public byte[] serialize(Object object) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryoThreadLocal.get().writeObject(output, object);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] array, Class<T> clazz) {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        Input input = new Input(byteArrayInputStream);
        T object = kryoThreadLocal.get().readObject(input, clazz);
        input.close();
        return object;
    }
}
