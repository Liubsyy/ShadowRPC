package com.liubs.shadowrpc.client.loadbalance;

import com.liubs.shadowrpc.client.connection.ShadowClient;
import com.liubs.shadowrpc.client.connection.ShadowClientGroup;

/**
 * @author Liubsyy
 * @date 2024/1/18
 **/
public class LoadBalanceContext {

    private PollingBalance pollingBalance;
    private ShadowClientGroup shadowClientGroup;

    public LoadBalanceContext(ShadowClientGroup shadowClientGroup) {
        this.shadowClientGroup = shadowClientGroup;
        pollingBalance = new PollingBalance(this);
    }


    public int numOfConnections() {
        return shadowClientGroup.getShadowClients().size();
    }

    public ShadowClient getBalanceShadowClient(){
        int nextBalance = pollingBalance.getNextBalance();
        return shadowClientGroup.getShadowClients().get(nextBalance);
    }
}
