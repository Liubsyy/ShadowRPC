package rpctest.hello;

import com.liubs.shadowrpc.clientmini.connection.ShadowClient;
import org.junit.Test;
import rpctest.entity.MyMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Liubsyy
 * @date 2024/1/20
 **/
public class HelloClient {

    /**
     * 调用hello方法
     */
    @Test
    public void helloClient() throws IOException, InterruptedException {
        ShadowClient shadowClient = new ShadowClient("127.0.0.1",2023);
        shadowClient.connect();

        IHello helloService = shadowClient.createRemoteProxy(IHello.class,"helloservice");

        System.out.println("发送 hello 消息");
        String helloResponse = helloService.hello("Tom");
        System.out.println("hello 服务端响应:"+helloResponse);

        MyMessage message = new MyMessage();
        message.setNum(100);
        message.setContent("Hello, Server!");

        System.out.printf("发送请求 : %s\n",message);
        MyMessage response = helloService.say(message);
        System.out.printf("接收服务端消息 : %s\n",response);

        shadowClient.close();
    }



    /**
     * 并发调用，测试拆包和粘包的可靠性
     */
    @Test
    public void helloConcurrent() throws InterruptedException, IOException {
        ShadowClient shadowClient = new ShadowClient("127.0.0.1",2023);
        shadowClient.connect();

        //调用远程RPC接口
        IHello helloService = shadowClient.createRemoteProxy(IHello.class,"helloservice");

        System.out.println("发送 hello 消息");
        String helloResponse = helloService.hello("Tom");
        System.out.println("hello 服务端响应:"+helloResponse);

        long time1 = System.currentTimeMillis();
        List<Callable<String>> futureTaskList = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(50);

        //100w个请求，32s
        final int n = 1000000;
        for(int i = 1;i<=n;i++) {
            final int j = i;
//            futureTaskList.add(() -> {

                try{
                    MyMessage message = new MyMessage();
                    message.setNum(j);
                    message.setContent("Hello, Server!");


                    Thread.sleep(500);

                    //打印消息影响速度，去掉打印至少快一倍
                    //System.out.printf("发送请求%d \n",j);
                    MyMessage response = helloService.say(message);
                     System.out.printf("接收服务端消息%s \n",response);
                }catch (Throwable e) {
                    e.printStackTrace();
                }
//                return "success";
//            });

        }


        executorService.invokeAll(futureTaskList);
        long time2 = System.currentTimeMillis();

        long useMs = (time2-time1);
        System.out.println("总共用时: "+useMs+" ms");

        executorService.shutdownNow();
        shadowClient.close();
    }
}
