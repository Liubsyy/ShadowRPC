package com.liubs.shadowrpc.client.loadbalance;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Liubsyy
 * @date 2024/1/18
 **/
public class PollingBalance implements IBalance {
    private LoadBalanceContext loadBalance;
    private AtomicInteger visitIndex = new AtomicInteger();


    public PollingBalance(LoadBalanceContext loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public int getNextBalance(String group){
        //原子性操作，获取下一个轮询节点
        return visitIndex.updateAndGet(c -> (c + 1) % loadBalance.numOfConnections(group));
    }
}
