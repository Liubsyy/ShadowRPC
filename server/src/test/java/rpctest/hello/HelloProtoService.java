package rpctest.hello;

import com.liubs.shadowrpc.base.config.ServerConfig;
import com.liubs.shadowrpc.base.constant.SerializerEnum;
import com.liubs.shadowrpc.base.annotation.ShadowService;
import com.liubs.shadowrpc.server.init.ServerBuilder;
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

        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setQpsStat(true); //统计qps
        serverConfig.setSerializer(SerializerEnum.PROTOBUF.name());
        serverConfig.setPort(2024);

        ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.entity")
                .addPackage("rpctest.hello")
                .build()
                .start().keep();
    }
}
