package com.liubs.shadowrpc.registry.listener;

import com.liubs.shadowrpc.registry.constant.ServerChangeType;
import com.liubs.shadowrpc.registry.entity.ServerNode;

/**
 * @author Liubsyy
 * @date 2023/12/31
 **/
public interface ServiceListener {

    /**
     * 服务节点变更事件
     * @param changeType
     * @param serverNode
     */
    void onServerChange(ServerChangeType changeType, ServerNode serverNode);
}
