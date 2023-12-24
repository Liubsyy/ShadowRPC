package com.liubs.shadowrpc.handler;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Liubsyy
 * @date 2023/12/3 11:32 PM
 **/
public class ReceiveHolder {
    public BlockingQueue<Object> queue = new LinkedBlockingQueue<>();

    private static ReceiveHolder instance = new ReceiveHolder();
    public static ReceiveHolder getInstance() {
        return instance;
    }


    public Object poll() throws InterruptedException {
        return queue.poll(3, TimeUnit.SECONDS);
    }

    public void put(Object message){
        queue.offer(message);
    }
}
