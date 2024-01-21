package com.liubs.shadowrpc.clientmini.nio;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Liubsyy
 * @date 2024/1/21
 **/
public class MessageSendFuture extends CompletableFuture<Integer> {
    private ByteBuffer buffer;

    public MessageSendFuture(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        MessageSendFuture messageSendFuture = new MessageSendFuture(null);

        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                messageSendFuture.completeExceptionally(new RuntimeException("aaa"));
            }
        }.start();

        try{
            messageSendFuture.get();
        }catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println(111);

    }
}
