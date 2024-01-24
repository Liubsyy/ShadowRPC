package rpctest.registry;

import com.liubs.shadowrpc.base.config.ServerConfig;
import com.liubs.shadowrpc.base.constant.SerializerEnum;
import com.liubs.shadowrpc.server.init.ServerBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 集群模式-protobuf
 * @author Liubsyy
 * @date 2024/1/24
 **/
public class SomeServersProtobuf {
    public static final String ZK_URL = "192.168.1.5:2181";

    private static ServerConfig serverConfig = new ServerConfig();

    @BeforeClass
    public static void init(){
        serverConfig.setGroup("MultiNodeGroup");
        serverConfig.setRegistryUrl(ZK_URL);
        serverConfig.setQpsStat(true); //统计qps
        serverConfig.setSerializer(SerializerEnum.PROTOBUF.name());
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

}
