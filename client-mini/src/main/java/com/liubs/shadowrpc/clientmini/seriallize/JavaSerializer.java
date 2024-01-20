package com.liubs.shadowrpc.clientmini.seriallize;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Liubsyy
 * @date 2024/1/20
 **/
public class JavaSerializer implements ISerializer {

    @Override
    public byte[] serialize(Object obj) {
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            baos.close();
            return baos.toByteArray();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T deserialize(byte[] array, Class<T> clazz) {
        try{
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
            // 将二进制字节流反序列化为对象
            final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            final T object =  (T)objectInputStream.readObject();
            // 关闭流
            objectInputStream.close();
            byteArrayInputStream.close();
            return object;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
