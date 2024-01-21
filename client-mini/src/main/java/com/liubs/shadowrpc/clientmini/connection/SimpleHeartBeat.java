package com.liubs.shadowrpc.clientmini.connection;

import com.liubs.shadowrpc.clientmini.logger.Logger;
import com.liubs.shadowrpc.clientmini.nio.MessageSendFuture;

import java.util.concurrent.ExecutionException;

/**
 * 简单的心跳
 * @author Liubsyy
 * @date 2024/1/21
 **/
public class SimpleHeartBeat extends Thread {
    private static Logger logger = Logger.getLogger(SimpleHeartBeat.class);

    private ShadowClient shadowClient;

    //心跳时间间隔
    private long heartBeatWaitSeconds;

    public SimpleHeartBeat(ShadowClient shadowClient, long heartBeatWaitSeconds) {
        this.shadowClient = shadowClient;
        this.heartBeatWaitSeconds = heartBeatWaitSeconds;
    }

    @Override
    public void run() {
        while(shadowClient.isRunning()) {

            try {
                Thread.sleep(heartBeatWaitSeconds);
            } catch (InterruptedException e) {
                break;
            }

            //发送心跳
            MessageSendFuture sendFuture = shadowClient.sendMessage(HeartBeatMessage.getHearBeatMsg());
            if(null != sendFuture) {
                try {
                    sendFuture.get();
                    logger.debug("send heart beat...");
                } catch (InterruptedException | ExecutionException e) {
                    sendFuture.cancel(true);
                    shadowClient.close();
                }
            }
        }
    }
}
