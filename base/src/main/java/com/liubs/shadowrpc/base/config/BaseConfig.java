package com.liubs.shadowrpc.base.config;

import com.liubs.shadowrpc.base.constant.SerializerEnum;

/**
 * @author Liubsyy
 * @date 2024/1/15
 */
public class BaseConfig {

    //序列化方式, 详见 com.liubs.shadowrpc.base.constant.SerializerEnum
    private String serializer = SerializerEnum.KRYO.name();

    //协议包最大上限(字节)
    private int maxFrameLength = 65535;

    //心跳开关
    private boolean heartBeat = true;

    //心跳时间间隔(默认10s)
    private int heartBeatWaitSeconds = 10;

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

    public boolean isHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(boolean heartBeat) {
        this.heartBeat = heartBeat;
    }

    public int getHeartBeatWaitSeconds() {
        return heartBeatWaitSeconds;
    }

    public void setHeartBeatWaitSeconds(int heartBeatWaitSeconds) {
        this.heartBeatWaitSeconds = heartBeatWaitSeconds;
    }
}
