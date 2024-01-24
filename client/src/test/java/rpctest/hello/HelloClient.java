package rpctest.hello;

import com.liubs.shadowrpc.base.config.ClientConfig;
import com.liubs.shadowrpc.base.module.ModulePool;
import com.liubs.shadowrpc.client.ClientModule;
import com.liubs.shadowrpc.client.connection.ShadowClient;
import com.liubs.shadowrpc.client.proxy.RemoteServerProxy;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpctest.entity.MyMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 这个例子证明了MyMessage新增字段，服务端能正常反序列化，然后服务端缺省了字段客户端也能反序列化
 * @author Liubsyy
 * @date 2023/12/18 10:59 PM
 **/

public class HelloClient {

    private static final Logger logger = LoggerFactory.getLogger(HelloClient.class);

    @BeforeClass
    public static void setUp() throws Exception {
        ModulePool.getModule(ClientModule.class).init(new ClientConfig());
    }


    /**
     * 调用hello方法
     */
    @Test
    public void helloClient() {
        ShadowClient shadowClient = new ShadowClient("127.0.0.1",2023);
        shadowClient.init();


        IHello helloService = shadowClient.createRemoteProxy(IHello.class,"shadowrpc://DefaultGroup/helloservice");

        logger.info("发送 hello 消息");
        String helloResponse = helloService.hello("Tom");
        logger.info("hello 服务端响应:"+helloResponse);

        MyMessage message = new MyMessage();
        message.setNum(100);
        message.setContent("Hello, Server!");

        System.out.printf("发送请求 : %s\n",message);
        MyMessage response = helloService.say(message);
        System.out.printf("接收服务端消息 : %s\n",response);
    }

    /**
     * 并发调用，测试拆包和粘包的可靠性
     */
    @Test
    public void helloConcurrent() throws InterruptedException {
        ShadowClient shadowClient = new ShadowClient("127.0.0.1",2023);
        shadowClient.init();

        //调用远程RPC接口
        IHello helloService = shadowClient.createRemoteProxy(IHello.class,"shadowrpc://DefaultGroup/helloservice");

        System.out.println("发送 hello 消息");
        String helloResponse = helloService.hello("Tom");
        System.out.println("hello 服务端响应:"+helloResponse);

        long time1 = System.currentTimeMillis();
        List<Callable<String>> futureTaskList = new ArrayList<>();

        //100w个请求，27s
        final int n = 1000000;
        for(int i = 1;i<=n;i++) {
            final int j = i;
            futureTaskList.add(() -> {
                MyMessage message = new MyMessage();
                message.setNum(j);
                message.setContent("Hello, Server!");


                //打印消息影响速度，去掉打印至少快一倍
                //System.out.printf("发送请求%d \n",j);
                MyMessage response = helloService.say(message);
                //System.out.printf("接收服务端消息%d \n",j);

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

    //测试心跳
    @Test
    public void testHeartBeat() throws InterruptedException {
        ClientConfig config = ModulePool.getModule(ClientModule.class).getConfig();
        config.setHeartBeat(true);
        config.setHeartBeatWaitSeconds(3);

        ShadowClient shadowClient = new ShadowClient("127.0.0.1",2023);
        shadowClient.init();
        while(true){
            Thread.sleep(1000);
        }
    }


}
