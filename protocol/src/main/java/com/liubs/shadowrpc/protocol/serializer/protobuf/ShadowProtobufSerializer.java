package com.liubs.shadowrpc.protocol.serializer.protobuf;

import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequestProto;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponseProto;

/**
 * @author Liubsyy
 * @date 2023/12/23 9:09 PM
 **/
public class ShadowProtobufSerializer extends ProtobufSerializerBase {

    private final ProtobufSerializer requestSerializer = new ProtobufSerializer(ShadowRPCRequestProto.ShadowRPCRequest.getDefaultInstance());
    private final ProtobufSerializer responseSerializer = new ProtobufSerializer(ShadowRPCResponseProto.ShadowRPCResponse.getDefaultInstance());


    @Override
    public <T> T deserialize(byte[] array, Class<T> clazz) {
        if(clazz == ShadowRPCRequestProto.ShadowRPCRequest.class) {
            return requestSerializer.deserialize(array,clazz);
        }
        if(clazz == ShadowRPCResponseProto.ShadowRPCResponse.class) {
            return responseSerializer.deserialize(array,clazz);
        }
        return null;
    }


}
