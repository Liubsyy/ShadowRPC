package com.liubs.shadowrpc.client.loadbalance;

import com.liubs.shadowrpc.client.connection.ShadowClient;
import com.liubs.shadowrpc.client.connection.ShadowClientGroup;

/**
 * @author Liubsyy
 * @date 2024/1/18
 **/
public class LoadBalanceContext {

    private String group;
    private PollingBalance pollingBalance;
    private ShadowClientGroup shadowClientGroup;

    public LoadBalanceContext(String group,ShadowClientGroup shadowClientGroup) {
        this.group = group;
        this.shadowClientGroup = shadowClientGroup;
        pollingBalance = new PollingBalance(this);
    }


    public int numOfConnections() {
        return shadowClientGroup.getShadowClients(group).size();
    }

    public ShadowClient getBalanceShadowClient(){
        int nextBalance = pollingBalance.getNextBalance();
        return shadowClientGroup.getShadowClients(group).get(nextBalance);
    }
}
