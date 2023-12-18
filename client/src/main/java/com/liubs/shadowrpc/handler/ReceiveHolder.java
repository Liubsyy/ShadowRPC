package com.liubs.shadowrpc.handler;

import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Liubsyy
 * @date 2023/12/3 11:32 PM
 **/
public class ReceiveHolder {
    public BlockingQueue<ShadowRPCResponse> queue = new LinkedBlockingQueue<>();

    private static ReceiveHolder instance = new ReceiveHolder();
    public static ReceiveHolder getInstance() {
        return instance;
    }


    public ShadowRPCResponse poll() throws InterruptedException {
        return queue.poll(3, TimeUnit.SECONDS);
    }

    public void put(ShadowRPCResponse message){
        queue.offer(message);
    }
}
