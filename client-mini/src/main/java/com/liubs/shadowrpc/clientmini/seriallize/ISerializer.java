package com.liubs.shadowrpc.clientmini.seriallize;


/**
 * @author Liubsyy
 * @date 2023/12/18 10:42 PM
 **/
public interface ISerializer {

    byte[] serialize(Object object);

    <T> T deserialize(byte[] array, Class<T> clazz);

}
