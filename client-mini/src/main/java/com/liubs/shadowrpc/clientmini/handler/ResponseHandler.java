package com.liubs.shadowrpc.clientmini.handler;

import com.liubs.shadowrpc.clientmini.logger.Logger;
import com.liubs.shadowrpc.clientmini.nio.IMessageListener;
import com.liubs.shadowrpc.clientmini.seriallize.ISerializer;
import com.liubs.shadowrpc.protocol.entity.JavaSerializeRPCResponse;

/**
 * @author Liubsyy
 * @date 2024/1/20
 **/

public class ResponseHandler implements IMessageListener {

    private static final int SUCCESS = 10;

    private static Logger logger = Logger.getLogger(ResponseHandler.class);
    
    private ISerializer serializer;

    public ResponseHandler(ISerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void handleMessage(byte[] bytes) {
        JavaSerializeRPCResponse response = serializer.deserialize(bytes, JavaSerializeRPCResponse.class);
        if(response == null) {
            return;
        }
        //接收消息
        ReceiveHolder.getInstance().receiveData(response);
    }
}
