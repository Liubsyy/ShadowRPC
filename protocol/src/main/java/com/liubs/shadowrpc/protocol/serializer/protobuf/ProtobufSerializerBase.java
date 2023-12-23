package com.liubs.shadowrpc.protocol.serializer.protobuf;

import com.google.protobuf.MessageLite;
import com.liubs.shadowrpc.protocol.serializer.ISerializer;

/**
 * @author Liubsyy
 * @date 2023/12/23 10:17 PM
 **/
public abstract class ProtobufSerializerBase implements ISerializer {

    @Override
    public byte[] serialize(Object object) {
        if (object instanceof MessageLite) {
            return ((MessageLite) object).toByteArray();
        }
        if (object instanceof MessageLite.Builder) {
            return (((MessageLite.Builder) object).build().toByteArray());
        }

        return new byte[0];
    }
}
