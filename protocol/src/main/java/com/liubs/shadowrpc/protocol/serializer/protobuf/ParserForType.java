package com.liubs.shadowrpc.protocol.serializer.protobuf;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liubsyy
 * @date 2023/12/24 1:05 AM
 **/
public class ParserForType {

    //所有protobuf的类型，ClassName=>MessageLite对象
    private static final Map<String,MessageLite> allProtobufMessages = new HashMap<>();

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

    public static void addMessage(String name,MessageLite messageLite) {
        allProtobufMessages.put(name,messageLite);
    }
    public static MessageLite getMessage(String name) {
        return allProtobufMessages.get(name);
    }


    public static <T> T parseFrom(MessageLite prototype,byte[] array) throws InvalidProtocolBufferException {
        return parseFrom(prototype,null,array);
    }

    public static <T> T parseFrom(MessageLite prototype, ExtensionRegistryLite extensionRegistry, byte[] array) throws InvalidProtocolBufferException {
        if (extensionRegistry == null) {
            if (HAS_PARSER) {
                return (T)prototype.getParserForType().parseFrom(array, 0, array.length);
            } else {
                return (T)prototype.newBuilderForType().mergeFrom(array, 0, array.length).build();
            }
        } else {
            if (HAS_PARSER) {
                return (T)prototype.getParserForType().parseFrom(
                        array, 0, array.length, extensionRegistry);
            } else {
                return (T)prototype.newBuilderForType().mergeFrom(
                        array, 0, array.length, extensionRegistry).build();
            }
        }

    }
}
