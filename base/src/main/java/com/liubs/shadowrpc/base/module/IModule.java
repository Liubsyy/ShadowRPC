package com.liubs.shadowrpc.base.module;

/**
 * @author Liubsyy
 * @date 2024/1/2
 */
public interface IModule {
    <P> boolean init(P param);
    void exit();
}
