package com.liubs.shadowrpc.config;

/**
 * 默认配置
 * @author Liubsyy
 * @date 2023/12/18 11:20 PM
 **/
public class ShadowServerConfig {
    private static ShadowServerConfig instance = new ShadowServerConfig();


    //协议包最大上限(字节)
    private int maxFrameLength = 65535;


    //心跳开关
    private boolean heartBeat = true;

    //心跳时间间隔(默认10s)
    private int heartBeatWaitSeconds = 10;


    private boolean qpsStat;



    public static ShadowServerConfig getInstance() {
        return instance;
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

    public static void setInstance(ShadowServerConfig instance) {
        ShadowServerConfig.instance = instance;
    }

    public int getMaxFrameLength() {
        return maxFrameLength;
    }

    public void setMaxFrameLength(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }


    public boolean isQpsStat() {
        return qpsStat;
    }

    public void setQpsStat(boolean qpsStat) {
        this.qpsStat = qpsStat;
    }
}
