package com.liubs.shadowrpc.protocol.serializer;


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


    public void setSerializer(SerializerEnum serializer) {
        this.serializer = serializer;
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
