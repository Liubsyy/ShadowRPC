package com.liubs.shadowrpc.research.serialize.strategy;

import java.util.Arrays;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2023/12/2 10:10 AM
 */
public interface ISerialize {

    List<ISerialize> SERIALIZES = Arrays.asList(new JavaSerialize()
            ,new FastJsonSerialize()
            ,new GsonSerialize()
            ,new KryoSerialize()
            ,new KryoSerializeRegister()
            ,new KryoSerializeCompatible()
            ,new KryoSerializeCompatibleRegister()
            ,new FSTSerialize()
            ,new HessianSerialize()
    );


    byte[] serialize(Object obj) throws Exception;
    <T> T unSerialize(Class<T> classz,byte[] bytes)throws Exception;
}
