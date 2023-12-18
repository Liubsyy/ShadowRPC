package rpctest.registry;

import com.liubs.shadowrpc.service.ServerManager;
import org.junit.Test;

/**
 * 注册中心和负载均衡测试
 * @author Liubsyy
 * @date 2023/12/18 11:48 PM
 **/
public class SomeServers {

    public static final String ZK_URL = "localhost:2181";

    @Test
    public void server1(){
        ServerManager.getInstance()
                .scanService("rpctest.hello")
                .startServer(ZK_URL,2023)
                .keep();
    }

    @Test
    public void server2(){
        ServerManager.getInstance()
                .scanService("rpctest.hello")
                .startServer(ZK_URL,2024)
                .keep();
    }

    @Test
    public void server3(){
        ServerManager.getInstance()
                .scanService("rpctest.hello")
                .startServer(ZK_URL,2025)
                .keep();
    }
}
