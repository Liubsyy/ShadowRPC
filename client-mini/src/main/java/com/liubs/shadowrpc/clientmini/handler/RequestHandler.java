package com.liubs.shadowrpc.clientmini.handler;

import com.liubs.shadowrpc.clientmini.seriallize.ISerializer;

/**
 * @author Liubsyy
 * @date 2024/1/20
 **/
public class RequestHandler {
    private ISerializer serializer;

    public RequestHandler(ISerializer serializer) {
        this.serializer = serializer;
    }

    public byte[] handleMessage(Object obj) {
        return serializer.serialize(obj);
    }
}
