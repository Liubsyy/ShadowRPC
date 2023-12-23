package com.liubs.shadowrpc.protocol.serializer;

import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequest;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequestProto;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponseProto;
import com.liubs.shadowrpc.protocol.serializer.kryo.KryoSerializer;
import com.liubs.shadowrpc.protocol.serializer.protobuf.ShadowProtobufSerializer;

/**
 * @author Liubsyy
 * @date 2023/12/23 6:36 PM
 **/
public enum SerializerEnum {

    KRYO(1, "kryo序列化",
            new KryoSerializer(),
            ShadowRPCRequest.class,
            ShadowRPCResponse.class),

    PROTOBUF(2,"protobuf序列化",
            new ShadowProtobufSerializer(),
            ShadowRPCRequestProto.ShadowRPCRequest.class,
            ShadowRPCResponseProto.ShadowRPCResponse.class),

    ;

    private int serializeType;
    private String text;
    private ISerializer serializer;
    private Class<?> requestClass;
    private Class<?> responseClass;


    SerializerEnum(int serializeType, String text, ISerializer serializer, Class<?> requestClass, Class<?> responseClass) {
        this.serializeType = serializeType;
        this.text = text;
        this.serializer = serializer;
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }


    public ISerializer getSerializer() {
        return serializer;
    }

    public Class<?> getRequestClass() {
        return requestClass;
    }

    public Class<?> getResponseClass() {
        return responseClass;
    }
}
