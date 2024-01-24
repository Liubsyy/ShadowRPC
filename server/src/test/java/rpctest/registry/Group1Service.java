package rpctest.registry;

import com.liubs.shadowrpc.base.annotation.ShadowService;

/**
 * @author Liubsyy
 * @date 2024/1/24
 **/
@ShadowService(serviceName = "groupService")
public class Group1Service implements IGroupService{
    @Override
    public String getGroupName() {
        return "group1";
    }
}
