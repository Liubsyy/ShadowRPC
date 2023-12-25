package com.liubs.shadowrpc.protocol.serializer;


import com.google.protobuf.MessageLite;
import com.liubs.shadowrpc.protocol.serializer.protobuf.ParserForType;
import com.liubs.shadowrpc.protocol.util.ClassScanWalker;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Liubsyy
 * @date 2023/12/23 5:01 PM
 **/
public class SerializerManager {
    private static SerializerManager instance = new SerializerManager();

    private SerializerEnum serializer = SerializerEnum.KRYO;

    public static SerializerManager getInstance() {
        return instance;
    }

    //初始化序列化模块
    public void init(String ...packageNames) {
        for(String packageName : packageNames) {
            try {
                ClassScanWalker.scanPackage(packageName,(classz)->{
                    if(MessageLite.class.isAssignableFrom(classz)) {
                        try {
                            MessageLite messageLite = (MessageLite)classz.getDeclaredMethod("getDefaultInstance").invoke(null);
                            ParserForType.addMessage(classz.getName(),messageLite);

                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                           e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void setSerializer(SerializerEnum serializer) {
        this.serializer = serializer;
    }


    public SerializerEnum getSerializer() {
        return serializer;
    }

    public byte[] serialize(Object obj) {
        return serializer.getSerializer().serialize(obj);
    }

    public <T> T deserializeRequest(byte[] bytes) {
        return (T)serializer.getSerializer().deserialize(bytes, serializer.getRequestClass());
    }

    public <T> Object deserializeResponse(byte[] bytes) {
        return (T)serializer.getSerializer().deserialize(bytes,serializer.getResponseClass());
    }



}
