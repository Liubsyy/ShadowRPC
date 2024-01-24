package com.liubs.shadowrpc.client.loadbalance;

/**
 * @author Liubsyy
 * @date 2024/1/18
 **/
public interface IBalance {
    int getNextBalance(String group);
}
