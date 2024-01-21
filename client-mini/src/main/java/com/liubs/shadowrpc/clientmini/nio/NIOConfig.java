package com.liubs.shadowrpc.clientmini.nio;

import com.liubs.shadowrpc.clientmini.seriallize.ISerializer;
import com.liubs.shadowrpc.clientmini.seriallize.JavaSerializer;

/**
 * @author Liubsyy
 * @date 2024/1/21
 **/
public class NIOConfig {

    //序列化 & 反序列化方式
    private ISerializer serializer = new JavaSerializer();

    //连接超时
    private long connectTimeout = 3000;

    //写入channel超时
    private long writeChannelTimeout = 1000;

    //同步调用超时
    private long waitTimeout = 3000;

    //心跳
    private boolean isHearBeat;

    //心跳间隔
    private long heartBeatWaitSeconds = 8000;

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getWaitTimeout() {
        return waitTimeout;
    }

    public void setWaitTimeout(long waitTimeout) {
        this.waitTimeout = waitTimeout;
    }

    public ISerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(ISerializer serializer) {
        this.serializer = serializer;
    }

    public long getWriteChannelTimeout() {
        return writeChannelTimeout;
    }

    public void setWriteChannelTimeout(long writeChannelTimeout) {
        this.writeChannelTimeout = writeChannelTimeout;
    }

    public boolean isHearBeat() {
        return isHearBeat;
    }

    public void setHearBeat(boolean hearBeat) {
        isHearBeat = hearBeat;
    }

    public long getHeartBeatWaitSeconds() {
        return heartBeatWaitSeconds;
    }

    public void setHeartBeatWaitSeconds(long heartBeatWaitSeconds) {
        this.heartBeatWaitSeconds = heartBeatWaitSeconds;
    }
}
