package rpctest.registry;

import com.liubs.shadowrpc.base.config.ServerConfig;
import com.liubs.shadowrpc.base.constant.SerializerEnum;
import com.liubs.shadowrpc.server.init.ServerBuilder;
import org.junit.Test;

/**
 * @author Liubsyy
 * @date 2024/1/24
 * 多集群模式单测
 **/
public class MultiGroupServers {

    public static final String ZK_URL = "localhost:2181";

    @Test
    public void group1service1(){
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setGroup("group1");
        serverConfig.setRegistryUrl(ZK_URL);
        serverConfig.setQpsStat(true); //统计qps
        serverConfig.setSerializer(SerializerEnum.KRYO.name());
        serverConfig.setPort(2023);
        ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.registry")
                .build()
                .start()
                .keep();
    }
    @Test
    public void group1service2(){
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setGroup("group1");
        serverConfig.setRegistryUrl(ZK_URL);
        serverConfig.setQpsStat(true); //统计qps
        serverConfig.setSerializer(SerializerEnum.KRYO.name());
        serverConfig.setPort(2024);
        ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.registry")
                .build()
                .start()
                .keep();
    }

    @Test
    public void group2service1(){
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setGroup("group2");
        serverConfig.setRegistryUrl(ZK_URL);
        serverConfig.setQpsStat(true); //统计qps
        serverConfig.setSerializer(SerializerEnum.KRYO.name());
        serverConfig.setPort(2025);
        ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.registry")
                .build()
                .start()
                .keep();
    }

    @Test
    public void group2service2(){
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setGroup("group2");
        serverConfig.setRegistryUrl(ZK_URL);
        serverConfig.setQpsStat(true); //统计qps
        serverConfig.setSerializer(SerializerEnum.KRYO.name());
        serverConfig.setPort(2026);
        ServerBuilder.newBuilder()
                .serverConfig(serverConfig)
                .addPackage("rpctest.registry")
                .build()
                .start()
                .keep();
    }




}
