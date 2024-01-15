package com.liubs.shadowrpc.base.config;

/**
 * @author Liubsyy
 * @date 2024/1/15
 */
public class ServerConfig extends BaseConfig {

    //心跳开关
    private boolean heartBeat = true;

    //心跳时间间隔(默认10s)
    private int heartBeatWaitSeconds = 10;

    //qps统计开关
    private boolean qpsStat;


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

    public boolean isQpsStat() {
        return qpsStat;
    }

    public void setQpsStat(boolean qpsStat) {
        this.qpsStat = qpsStat;
    }
}
