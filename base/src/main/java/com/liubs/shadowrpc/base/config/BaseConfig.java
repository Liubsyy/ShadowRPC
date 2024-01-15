package com.liubs.shadowrpc.base.config;

/**
 * @author Liubsyy
 * @date 2024/1/15
 */
public class BaseConfig {

    //序列化方式, 详见 com.liubs.shadowrpc.base.constant.SerializerEnum
    private String serializer;

    //协议包最大上限(字节)
    private int maxFrameLength = 65535;


    public String getSerializer() {
        return serializer;
    }

    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }

    public int getMaxFrameLength() {
        return maxFrameLength;
    }

    public void setMaxFrameLength(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }
}
