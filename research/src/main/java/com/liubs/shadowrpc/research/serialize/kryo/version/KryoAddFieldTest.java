package com.liubs.shadowrpc.research.serialize.kryo.version;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;
import com.liubs.shadowrpc.research.entity.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Kryo增删字段时，会导致序列化失败问题，需要考虑版本兼容性问题
 * @author Liubsyy
 * @date 2023/12/2 4:55 PM
 */
public class KryoAddFieldTest {

    public static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);    //不需要提前注册
        return kryo;
    });

    public static byte[] serialize(Object obj) throws Exception {
        Kryo kryo = kryoThreadLocal.get();

        ByteArrayOutputStream kryoBos = new ByteArrayOutputStream();
        Output output = new Output(kryoBos);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return kryoBos.toByteArray();
    }

    public static <T> T unSerialize(Class<T> classz, byte[] bytes) throws Exception {
        Kryo kryo = kryoThreadLocal.get();
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            return (T)kryo.readClassAndObject(input);
        }
    }


    public static void main(String[] args) throws Exception {

        //没有beat字段和有beat字段的序列化
        //[1, 0, 99, 111, 109, 46, 108, 105, 117, 98, 115, 46, 115, 104, 97, 100, 111, 119, 114, 112, 99, 46, 114, 101, 115, 101, 97, 114, 99, 104, 46, 101, 110, 116, 105, 116, 121, 46, 68, 121, 110, 97, 109, 105, 99, 77, 97, -18, 44, -44, 2, 0, 0, 0, 0, 0, -120, -61, 64, 84, 111, -19]
        //[1, 0, 99, 111, 109, 46, 108, 105, 117, 98, 115, 46, 115, 104, 97, 100, 111, 119, 114, 112, 99, 46, 114, 101, 115, 101, 97, 114, 99, 104, 46, 101, 110, 116, 105, 116, 121, 46, 68, 121, 110, 97, 109, 105, 99, 77, 97, -18, 44, -24, 7, -44, 2, 0, 0, 0, 0, 0, -120, -61, 64, 84, 111, -19]
        DynamicMan dynamicMan = new DynamicMan();
        dynamicMan.setName("Tom");
        dynamicMan.setAge(22);
        dynamicMan.setHeight(170L);
        dynamicMan.setMoney(10000D);

        byte[] bytes = serialize(dynamicMan);
        System.out.println("序列化: size="+bytes.length);
        System.out.println(Arrays.toString(bytes));

        DynamicMan unSerializeMan = unSerialize(DynamicMan.class, new byte[]{1, 0, 99, 111, 109, 46, 108, 105, 117, 98, 115, 46, 115, 104, 97, 100, 111, 119, 114, 112, 99, 46, 114, 101, 115, 101, 97, 114, 99, 104, 46, 101, 110, 116, 105, 116, 121, 46, 68, 121, 110, 97, 109, 105, 99, 77, 97, -18, 44, -24, 7, -44, 2, 0, 0, 0, 0, 0, -120, -61, 64, 84, 111, -19});
        System.out.println("反序列化: "+new Gson().toJson(unSerializeMan));

    }
}
