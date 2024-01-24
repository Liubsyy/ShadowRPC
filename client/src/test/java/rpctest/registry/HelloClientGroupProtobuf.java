package rpctest.registry;

import com.liubs.shadowrpc.base.config.ClientConfig;
import com.liubs.shadowrpc.base.module.ModulePool;
import com.liubs.shadowrpc.client.ClientModule;
import com.liubs.shadowrpc.client.connection.ShadowClient;
import com.liubs.shadowrpc.client.connection.ShadowClientGroup;
import com.liubs.shadowrpc.protocol.serializer.SerializerStrategy;
import org.junit.Before;
import org.junit.Test;
import rpctest.entity.MyMessageProto;
import rpctest.hello.IHelloProto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Liubsyy
 * @date 2024/1/24
 **/
public class HelloClientGroupProtobuf {

    private static final String ZK_URL = "192.168.1.5:2181";
    private static ClientConfig config;

    @Before
    public void init(){
        config = new ClientConfig();
        config.setSerializer(SerializerStrategy.PROTOBUF.name());
        ModulePool.getModule(ClientModule.class).init(config);
    }

    //接入注册中心，负载均衡调用rpc接口
    @Test
    public void connectRegistryForServices()  {
        ShadowClientGroup shadowClientGroup = new ShadowClientGroup(ZK_URL);
        shadowClientGroup.init();

        IHelloProto helloService = shadowClientGroup.createRemoteProxy(IHelloProto.class, "shadowrpc://MultiNodeGroup/helloprotoservice");
        List<ShadowClient> shadowClientList = shadowClientGroup.getShadowClients("MultiNodeGroup");

        System.out.println("所有服务器: "+shadowClientList.stream().map(c-> c.getRemoteIp()+":"+c.getRemotePort()).collect(Collectors.toList()));

        MyMessageProto.MyMessage message =  MyMessageProto.MyMessage.newBuilder()
                .setNum(100)
                .setContent("Hello, Server!")
                .build();

        for(int i = 0 ;i<shadowClientList.size() * 5; i++) {
            MyMessageProto.MyMessage myMessage = helloService.say(message);
            System.out.println(myMessage);
        }
    }


    /**
     * 并发调用，测试集群下的性能
     */
    @Test
    public void helloConcurrent() throws InterruptedException {

        ShadowClientGroup shadowClientGroup = new ShadowClientGroup(ZK_URL);
        shadowClientGroup.init();

        IHelloProto helloProtoService = shadowClientGroup.createRemoteProxy(IHelloProto.class, "shadowrpc://MultiNodeGroup/helloprotoservice");

        System.out.println("所有服务器: "+shadowClientGroup.getShadowClients("MultiNodeGroup").stream().map(c-> c.getRemoteIp()+":"+c.getRemotePort()).collect(Collectors.toList()));

        MyMessageProto.MyMessage requestMsg =  MyMessageProto.MyMessage.newBuilder()
                .setNum(100)
                .setContent("Hello, Server!")
                .build();

        System.out.printf("发送请求 : %s\n",requestMsg);
        MyMessageProto.MyMessage responseMsg = helloProtoService.say(requestMsg);
        System.out.printf("接收服务端消息 : %s\n",responseMsg);


        long time1 = System.currentTimeMillis();
        List<Callable<String>> futureTaskList = new ArrayList<>();


        //100w个请求，25s
        final int n = 1000000;
        for(int i = 1;i<=n;i++) {
            final int j = i;
            futureTaskList.add(() -> {
                MyMessageProto.MyMessage message = MyMessageProto.MyMessage.newBuilder().setNum(j).setContent("Hello, Server!").build();
                //打印消息影响速度，去掉打印至少快一倍
                MyMessageProto.MyMessage response = helloProtoService.say(message);
                return "success";
            });
        }

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        executorService.invokeAll(futureTaskList);
        long time2 = System.currentTimeMillis();

        long useMs = (time2-time1);
        System.out.println("总共用时: "+useMs+" ms");

        executorService.shutdownNow();

        shadowClientGroup.close();
    }
}
