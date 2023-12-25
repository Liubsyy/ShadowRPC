package rpctest.hello;

import com.liubs.shadowrpc.config.ShadowClientConfig;
import com.liubs.shadowrpc.init.ShadowClient;
import com.liubs.shadowrpc.init.ShadowClientsManager;
import com.liubs.shadowrpc.proxy.RemoteServerProxy;
import org.junit.Test;
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


    /**
     * 调用hello方法
     */
    @Test
    public void helloClient() {
        ShadowClient shadowClient = new ShadowClient();
        shadowClient.init("127.0.0.1",2023);


        IHello helloService = RemoteServerProxy.create(shadowClient.getChannel(),IHello.class,"helloservice");

        System.out.println("发送 hello 消息");
        String helloResponse = helloService.hello("Tom");
        System.out.println("hello 服务端响应:"+helloResponse);

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
        ShadowClient shadowClient = new ShadowClient();
        shadowClient.init("127.0.0.1",2023);

        //调用远程RPC接口
        IHello helloService = RemoteServerProxy.create(shadowClient.getChannel(),IHello.class,"helloservice");

        System.out.println("发送 hello 消息");
        String helloResponse = helloService.hello("Tom");
        System.out.println("hello 服务端响应:"+helloResponse);

        long time1 = System.currentTimeMillis();
        List<Callable<String>> futureTaskList = new ArrayList<>();

        //100w个请求，32s
        final int n = 1000000;
        for(int i = 1;i<=n;i++) {
            final int j = i;
            futureTaskList.add(() -> {
                MyMessage message = new MyMessage();
                message.setNum(j);
                message.setContent("Hello, Server!");

                System.out.printf("发送请求%d : %s\n",j,message);
                MyMessage response = helloService.say(message);
                System.out.printf("接收服务端消息%d : %s\n",j,response);

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
        ShadowClientConfig.getInstance().setHeartBeat(true);
        ShadowClientConfig.getInstance().setHeartBeatWaitSeconds(3);
        ShadowClient shadowClient = new ShadowClient();
        shadowClient.init("127.0.0.1",2023);
        while(true){
            Thread.sleep(1000);
        }
    }


}
