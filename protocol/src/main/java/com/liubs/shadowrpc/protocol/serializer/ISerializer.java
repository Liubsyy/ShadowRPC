package com.liubs.shadowrpc.protocol.serializer;

import com.liubs.shadowrpc.protocol.serializer.kryo.KryoSerializer;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:42 PM
 **/
public interface ISerializer {
    ISerializer DEFAULT_SERIALIZER = new KryoSerializer();

    byte[] serialize(Object object);

    <T> T deserialize(byte[] array, Class<T> clazz);

}
