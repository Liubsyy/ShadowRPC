package com.liubs.shadowrpc.protocol.serializer.protobuf;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.MessageLite;

/**
 * protobuf序列化和反序列化
 * @author Liubsyy
 * @date 2023/12/23 4:59 PM
 **/
public class ProtobufSerializer extends ProtobufSerializerBase {

    private MessageLite prototype;
    private ExtensionRegistryLite extensionRegistry;


    public ProtobufSerializer(MessageLite prototype) {
        this(prototype, null);
    }

    public ProtobufSerializer(MessageLite prototype, ExtensionRegistry extensionRegistry) {
        this(prototype, (ExtensionRegistryLite) extensionRegistry);
    }

    public ProtobufSerializer(MessageLite prototype, ExtensionRegistryLite extensionRegistry) {
        this.prototype = prototype.getDefaultInstanceForType();
        this.extensionRegistry = extensionRegistry;
    }


    @Override
    public <T> T deserialize(byte[] array, Class<T> clazz) {
        try{
           return ParserForType.parseFrom(prototype,extensionRegistry,array);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
