package com.liubs.shadowrpc.client.connection;

import io.netty.channel.Channel;

/**
 * @author Liubsyy
 * @date 2024/1/17
 **/
public interface IConnection {
    void init();

    Channel getChannel();

    void close();
}
