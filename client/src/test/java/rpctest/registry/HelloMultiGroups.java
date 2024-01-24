package rpctest.registry;

import com.liubs.shadowrpc.base.config.ClientConfig;
import com.liubs.shadowrpc.base.module.ModulePool;
import com.liubs.shadowrpc.client.ClientModule;
import com.liubs.shadowrpc.client.connection.ShadowClientGroup;
import com.liubs.shadowrpc.protocol.serializer.SerializerStrategy;
import org.junit.Before;
import org.junit.Test;

/**
 * 多集群模式测试
 * @author Liubsyy
 * @date 2024/1/24
 **/
public class HelloMultiGroups {
    private static ClientConfig config;

    @Before
    public void init(){
        config = new ClientConfig();
        config.setSerializer(SerializerStrategy.KRYO.name());
        ModulePool.getModule(ClientModule.class).init(config);
    }

    @Test
    public void hello(){
        ShadowClientGroup shadowClientGroup = new ShadowClientGroup("localhost:2181");
        shadowClientGroup.init();

        IGroupService group1 = shadowClientGroup.createRemoteProxy(IGroupService.class, "shadowrpc://group1/groupService");
        for(int i=0,len=shadowClientGroup.getShadowClients("group1").size(); i<len ;i++) {
            System.out.println("groupName:"+group1.getGroupName());
        }

        IGroupService group2 = shadowClientGroup.createRemoteProxy(IGroupService.class, "shadowrpc://group2/groupService2");
        for(int i=0,len=shadowClientGroup.getShadowClients("group2").size(); i<len ;i++) {
            System.out.println("groupName:"+group2.getGroupName());
        }

    }
}
