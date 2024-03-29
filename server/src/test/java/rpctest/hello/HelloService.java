package rpctest.hello;

import com.liubs.shadowrpc.base.annotation.ShadowService;
import com.liubs.shadowrpc.base.config.ServerConfig;
import com.liubs.shadowrpc.base.constant.SerializerEnum;
import com.liubs.shadowrpc.server.init.ServerBuilder;
import org.junit.Test;
import rpctest.entity.MyMessage;

/**
 * 一个helloservice服务
 * @author Liubsyy
 * @date 2023/12/18 11:01 PM
 **/
@ShadowService(serviceName = "helloservice")
public class HelloService implements IHello {


    @Override
    public String hello(String msg) {
        return "Hello,"+msg;
    }

    @Override
    public MyMessage say(MyMessage message) {
        MyMessage message1 = new MyMessage();
        message1.setContent("hello received "+"("+message.getContent()+")");
        message1.setNum(message.getNum()+1);
        return message1;
    }


    //java原生序列化服务器启动服务端
    @Test
    public void helloServiceStartForJavaSerialize() {

        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setQpsStat(true); //统计qps
        serverConfig.setSerializer(SerializerEnum.JAVA_SERIALISE.getSerializeType());
        serverConfig.setPort(2023);

        ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.hello")
                .build()
                .start()
                .keep();

    }

    //启动服务端
    @Test
    public void helloServiceStart() {

        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setQpsStat(true); //统计qps
        serverConfig.setPort(2023);

        ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.hello")
                .build()
                .start()
                .keep();

    }

}
