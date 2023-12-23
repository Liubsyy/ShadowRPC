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
    private static final boolean HAS_PARSER;

    static {
        boolean hasParser = false;
        try {
            // MessageLite.getParserForType() is not available until protobuf 2.5.0.
            MessageLite.class.getDeclaredMethod("getParserForType");
            hasParser = true;
        } catch (Throwable t) {
            // Ignore
        }

        HAS_PARSER = hasParser;
    }

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
            final int length = array.length;

            T t;
            if (extensionRegistry == null) {
                if (HAS_PARSER) {
                    t =  (T)prototype.getParserForType().parseFrom(array, 0, length);
                } else {
                    t =  (T)prototype.newBuilderForType().mergeFrom(array, 0, length).build();
                }
            } else {
                if (HAS_PARSER) {
                    t =  (T)prototype.getParserForType().parseFrom(
                            array, 0, length, extensionRegistry);
                } else {
                    t =  (T)prototype.newBuilderForType().mergeFrom(
                            array, 0, length, extensionRegistry).build();
                }
            }

            return t;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
