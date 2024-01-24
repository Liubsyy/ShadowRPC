package rpctest.registry;

import com.liubs.shadowrpc.base.annotation.ShadowInterface;

/**
 * @author Liubsyy
 * @date 2024/1/24
 **/
@ShadowInterface
public interface IGroupService {

    String getGroupName();
}
