package rpctest.hello;

import com.liubs.shadowrpc.protocol.annotation.ShadowService;
import com.liubs.shadowrpc.service.ServerManager;
import org.junit.Test;
import rpctest.entity.MyMessageProto;

/**
 * 一个基于protobuf的helloservice服务
 *
 * @author Liubsyy
 * @date 2023/12/25 12:48 AM
 **/
@ShadowService(serviceName = "helloprotoservice")
public class HelloProtoService implements IHelloProto{

    @Override
    public MyMessageProto.MyMessage say(MyMessageProto.MyMessage message) {

        return MyMessageProto.MyMessage.newBuilder()
                .setContent("hello received "+"("+message.getContent()+")")
                .setNum(message.getNum()+1)
                .build();

    }

    //启动服务端
    @Test
    public void helloServiceStart() {
        ServerManager.getInstance()
                .scanService("rpctest.entity","rpctest.hello")
                .startServer(2023)
                .keep();
    }
}
