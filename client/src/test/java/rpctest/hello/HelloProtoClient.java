package rpctest.hello;

import com.liubs.shadowrpc.init.ShadowClient;
import com.liubs.shadowrpc.proxy.RemoteServerProxy;
import org.junit.Test;
import rpctest.entity.MyMessageProto;

/**
 * 基于protobuf服务的调用
 * @author Liubsyy
 * @date 2023/12/25 12:55 AM
 **/
public class HelloProtoClient {

    @Test
    public void helloClient() {
        ShadowClient shadowClient = new ShadowClient();
        shadowClient.init("127.0.0.1",2023);


        IHelloProto helloService = RemoteServerProxy.create(shadowClient.getChannel(),IHelloProto.class,"helloprotoservice");

        MyMessageProto.MyMessage message =  MyMessageProto.MyMessage.newBuilder()
                .setNum(100)
                .setContent("Hello, Server!")
                .build();

        System.out.printf("发送请求 : %s\n",message);
        MyMessageProto.MyMessage response = helloService.say(message);
        System.out.printf("接收服务端消息 : %s\n",response);
    }
}
