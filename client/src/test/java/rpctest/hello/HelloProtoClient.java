package rpctest.hello;

import com.liubs.shadowrpc.init.ShadowClient;
import com.liubs.shadowrpc.protocol.serializer.SerializerEnum;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import com.liubs.shadowrpc.proxy.RemoteServerProxy;
import org.junit.Before;
import org.junit.Test;
import rpctest.entity.MyMessageProto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于protobuf服务的调用
 * @author Liubsyy
 * @date 2023/12/25 12:55 AM
 **/
public class HelloProtoClient {


    @Before
    public void init(){
        SerializerManager.getInstance().setSerializer(SerializerEnum.PROTOBUF);
        SerializerManager.getInstance().init("rpctest.entity");
    }

    @Test
    public void helloClient() {

        ShadowClient shadowClient = new ShadowClient();
        shadowClient.init("127.0.0.1",2024);

        IHelloProto helloService = RemoteServerProxy.create(shadowClient,IHelloProto.class,"helloprotoservice");
        MyMessageProto.MyMessage message =  MyMessageProto.MyMessage.newBuilder()
                .setNum(100)
                .setContent("Hello, Server!")
                .build();

        System.out.printf("发送请求 : %s\n",message);
        MyMessageProto.MyMessage response = helloService.say(message);
        System.out.printf("接收服务端消息 : %s\n",response);
        shadowClient.close();

    }

    /**
     * 并发调用，测试拆包和粘包的可靠性
     */
    @Test
    public void helloConcurrent() throws InterruptedException {

        ShadowClient shadowClient = new ShadowClient();
        shadowClient.init("127.0.0.1",2024);

        //调用远程RPC接口
        IHelloProto helloService = RemoteServerProxy.create(shadowClient,IHelloProto.class,"helloprotoservice");


        MyMessageProto.MyMessage requestMsg =  MyMessageProto.MyMessage.newBuilder()
                .setNum(100)
                .setContent("Hello, Server!")
                .build();

        System.out.printf("发送请求 : %s\n",requestMsg);
        MyMessageProto.MyMessage responseMsg = helloService.say(requestMsg);
        System.out.printf("接收服务端消息 : %s\n",responseMsg);


        long time1 = System.currentTimeMillis();
        List<Callable<String>> futureTaskList = new ArrayList<>();


        //100w个请求，13s
        final int n = 1000000;
        for(int i = 1;i<=n;i++) {
            final int j = i;
            futureTaskList.add(() -> {
                MyMessageProto.MyMessage message = MyMessageProto.MyMessage.newBuilder().setNum(j).setContent("Hello, Server!").build();

                //打印消息影响速度，去掉打印至少快一倍
                //System.out.printf("发送请求%d \n",j);
                MyMessageProto.MyMessage response = helloService.say(message);
//                System.out.printf("接收服务端消息%d \n",j);

                return "success";
            });
        }

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        executorService.invokeAll(futureTaskList);
        long time2 = System.currentTimeMillis();

        long useMs = (time2-time1);
        System.out.println("总共用时: "+useMs+" ms");

        executorService.shutdownNow();
        shadowClient.close();
    }
}
