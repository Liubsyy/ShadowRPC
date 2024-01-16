package com.liubs.shadowrpc.client;

import com.liubs.shadowrpc.base.annotation.ModuleInject;
import com.liubs.shadowrpc.base.annotation.ShadowModule;
import com.liubs.shadowrpc.base.config.ClientConfig;
import com.liubs.shadowrpc.base.module.IModule;
import com.liubs.shadowrpc.protocol.SerializeModule;

import java.util.Collections;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2024/1/16
 */

@ShadowModule
public class ClientModule implements IModule {

    private ClientConfig config;

    @ModuleInject
    private SerializeModule serializeModule;

    public void init(ClientConfig config){
       init(config, Collections.EMPTY_LIST);
    }
    public void init(ClientConfig config, List<String> packages){
        this.config = config;
        serializeModule.init(config,packages);
    }
}
