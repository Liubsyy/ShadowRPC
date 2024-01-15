package rpctest.hello;

import com.liubs.shadowrpc.config.ShadowServerConfig;
import com.liubs.shadowrpc.base.annotation.ShadowService;
import com.liubs.shadowrpc.protocol.serializer.SerializerStrategy;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
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
        ShadowServerConfig.getInstance().setQpsStat(true);  //统计qps
        SerializerManager.getInstance().setSerializer(SerializerStrategy.PROTOBUF); //protobuf序列化方式
        ServerManager.getInstance()
                .scanService("rpctest.entity","rpctest.hello")
                .startServer(2024)
                .keep();
    }
}
