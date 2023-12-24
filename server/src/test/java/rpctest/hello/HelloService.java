package rpctest.hello;

import com.liubs.shadowrpc.protocol.annotation.ShadowService;
import com.liubs.shadowrpc.service.ServerManager;
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


    //启动服务端
    @Test
    public void helloServiceStart() {
        ServerManager.getInstance()
                .scanService("rpctest.hello")
                .startServer(2023)
                .keep();
    }

}
