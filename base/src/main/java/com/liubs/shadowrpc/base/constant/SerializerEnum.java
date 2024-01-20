package com.liubs.shadowrpc.base.constant;

/**
 * @author Liubsyy
 * @date 2024/1/15
 */
public enum SerializerEnum {
    KRYO("KRYO","kryo序列化"),
    PROTOBUF("PROTOBUF","protobuf序列化"),
    JAVA_SERIALISE("JAVA_SERIALIZE","java原生序列化"),

    ;

    private String serializeType;
    private String text;

    SerializerEnum(String serializeType, String text) {
        this.serializeType = serializeType;
        this.text = text;
    }

    public static SerializerEnum findByType(String serializeType) {
        for(SerializerEnum e : values()) {
            if(e.serializeType.equals(serializeType)) {
                return e;
            }
        }
        return null;
    }

    public String getSerializeType() {
        return serializeType;
    }
}
