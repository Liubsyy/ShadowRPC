package rpctest.registry;

import com.liubs.shadowrpc.base.config.ServerConfig;
import com.liubs.shadowrpc.base.constant.SerializerEnum;
import com.liubs.shadowrpc.protocol.serializer.SerializerStrategy;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import com.liubs.shadowrpc.server.init.ServerBuilder;
import com.liubs.shadowrpc.server.service.ServerManager;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 注册中心和负载均衡测试
 * @author Liubsyy
 * @date 2023/12/18 11:48 PM
 **/
public class SomeServers {

    public static final String ZK_URL = "localhost:2181";

    private static ServerConfig serverConfig = new ServerConfig();

    @BeforeClass
    public static void init(){

        serverConfig.setRegistryUrl(ZK_URL);
        serverConfig.setQpsStat(true); //统计qps
        serverConfig.setSerializer(SerializerEnum.KRYO.name());

    }

    @Test
    public void server1(){
        serverConfig.setPort(2023);
        ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.hello")
                .build()
                .start()
                .keep();
    }

    @Test
    public void server2(){
        serverConfig.setPort(2024);
        ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.hello")
                .build()
                .start()
                .keep();
    }

    @Test
    public void server3(){
        serverConfig.setPort(2025);
        ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.hello")
                .build()
                .start()
                .keep();
    }
}
