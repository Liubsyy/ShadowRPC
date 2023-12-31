package com.liubs.shadowrpc.research.serialize.strategy;

import org.nustaq.serialization.FSTConfiguration;

/**
 * @author Liubsyy
 * @date 2023/12/2 3:36 PM
 */
public class FSTSerialize implements ISerialize{
    private static FSTConfiguration configuration = FSTConfiguration.createDefaultConfiguration();


    @Override
    public byte[] serialize(Object obj) throws Exception {
        return configuration.asByteArray(obj);
    }

    @Override
    public <T> T unSerialize(Class<T> classz, byte[] bytes) throws Exception {
        return (T)configuration.asObject(bytes);
    }
}
