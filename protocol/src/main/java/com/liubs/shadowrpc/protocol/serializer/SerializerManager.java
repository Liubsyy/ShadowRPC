package com.liubs.shadowrpc.protocol.serializer;


import com.google.protobuf.MessageLite;
import com.liubs.shadowrpc.base.config.BaseConfig;
import com.liubs.shadowrpc.protocol.serializer.protobuf.ParserForType;
import com.liubs.shadowrpc.base.util.ClassScanWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Liubsyy
 * @date 2023/12/23 5:01 PM
 **/
public class SerializerManager {
    private static final Logger logger = LoggerFactory.getLogger(SerializerManager.class);

    private static SerializerManager instance = new SerializerManager();

    private BaseConfig config;
    private SerializerStrategy serializer = SerializerStrategy.KRYO;

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
                logger.error("序列化包初始化失败",e);
            }
        }
    }


    public void setSerializer(SerializerStrategy serializer) {
        this.serializer = serializer;
    }


    public SerializerStrategy getSerializer() {
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
