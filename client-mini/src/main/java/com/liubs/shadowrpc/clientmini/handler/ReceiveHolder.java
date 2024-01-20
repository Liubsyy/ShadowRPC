package com.liubs.shadowrpc.clientmini.handler;

import com.liubs.shadowrpc.protocol.entity.JavaSerializeRPCResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @author Liubsyy
 * @date 2023/12/3 11:32 PM
 **/
public class ReceiveHolder {
    public Map<String, CompletableFuture<Object>> futureMap = new ConcurrentHashMap<>();

    private static ReceiveHolder instance = new ReceiveHolder();
    public static ReceiveHolder getInstance() {
        return instance;
    }


    public Future<?> initFuture(String uuid){
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        futureMap.put(uuid,completableFuture);
        return completableFuture;
    }

    public void deleteWait(String uuid) {
        futureMap.remove(uuid);
    }


    public void receiveData(JavaSerializeRPCResponse responseModel){
        CompletableFuture<Object> future = futureMap.remove(responseModel.getTraceId());
        if(null != future) {
            future.complete(responseModel);
        }
    }

}
