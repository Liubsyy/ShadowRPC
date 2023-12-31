package com.liubs.shadowrpc.research.serialize.strategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Liubsyy
 * @date 2023/12/2 10:11 AM
 */
public class JavaSerialize implements ISerialize {



    @Override
    public byte[] serialize(Object obj) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        baos.close();
        return baos.toByteArray();
    }

    @Override
    public <T> T unSerialize(Class<T> classz,byte[] bytes)throws Exception{
        // 字节输入流
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // 将二进制字节流反序列化为对象
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        final T object =  (T)objectInputStream.readObject();
        // 关闭流
        objectInputStream.close();
        byteArrayInputStream.close();
        return object;
    }
}
