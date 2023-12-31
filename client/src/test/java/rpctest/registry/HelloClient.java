package rpctest.registry;

import com.liubs.shadowrpc.init.ShadowClient;
import com.liubs.shadowrpc.init.ShadowClientsManager;
import com.liubs.shadowrpc.proxy.RemoteServerProxy;
import org.junit.Test;
import rpctest.hello.IHello;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liubsyy
 * @date 2023/12/18 11:50 PM
 **/
public class HelloClient {

    //接入注册中心，负载均衡调用rpc接口
    @Test
    public void connectRegistryForServices()  {
        ShadowClientsManager.getInstance().connectRegistry("localhost:2181");
        List<ShadowClient> shadowClientList = ShadowClientsManager.getInstance().getShadowClients();

        System.out.println("所有服务器: "+shadowClientList.stream().map(c-> c.getRemoteIp()+":"+c.getRemotePort()).collect(Collectors.toList()));

        IHello helloService = RemoteServerProxy.create(IHello.class,"helloservice");

        int helloCount = shadowClientList.size() * 5;
        for(int i = 0 ;i<helloCount; i++) {
            String hello = helloService.hello(i + "");
            System.out.println(hello);
        }
    }
}
