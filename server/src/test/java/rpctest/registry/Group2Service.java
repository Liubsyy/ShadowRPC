package rpctest.registry;

import com.liubs.shadowrpc.base.annotation.ShadowService;

/**
 * @author Liubsyy
 * @date 2024/1/24
 **/
@ShadowService(serviceName = "groupService2")
public class Group2Service implements IGroupService{
    @Override
    public String getGroupName() {
        return "group2";
    }
}
