package com.liubs.shadowrpc.clientmini.connection;

import com.liubs.shadowrpc.clientmini.handler.RequestHandler;
import com.liubs.shadowrpc.clientmini.handler.ResponseHandler;
import com.liubs.shadowrpc.clientmini.nio.NIOClient;
import com.liubs.shadowrpc.clientmini.seriallize.ISerializer;
import com.liubs.shadowrpc.clientmini.seriallize.JavaSerializer;

import java.io.IOException;

/**
 * @author Liubsyy
 * @date 2024/1/20
 **/
public class ShadowClient {

    //序列化 & 反序列化方式
    private ISerializer serializer = new JavaSerializer();

    private String host;
    private int port;

    private NIOClient nioClient;
    private RequestHandler requestHandler;
    private ResponseHandler responseHandler;



    public ShadowClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.requestHandler = new RequestHandler(serializer);
        this.responseHandler = new ResponseHandler(serializer);
        this.nioClient = new NIOClient(host,port,responseHandler);
    }

    public void connect() throws IOException {
        nioClient.connect();
    }


    public <T> T createRemoteProxy(Class<T> serviceStub, String serviceName) {
        return RemoteProxy.create(this,serviceStub,serviceName);
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public void sendMessage(byte[] bytes) {
        nioClient.sendMessage(bytes);
    }

    public boolean isRunning(){
        return nioClient.isRunning();
    }

    public void close(){
        nioClient.close();
    }


}
