package com.liubs.shadowrpc.registry.constant;


/**
 * @author Liubsyy
 * @date 2023/12/18 11:35 PM
 **/
public class ServiceRegistryPath {

    //服务基本路径
    public static final String BASE_PATH = "/shadowrpc/services";


    /**
     * 服务节点目录
     * @param group
     * @param serverUniqueKey
     * @return
     */
    public static String getServerNodePath(String group,String serverUniqueKey) {
        return String.format("%s/%s/%s",BASE_PATH,group,serverUniqueKey);
    }


    public static String uniqueKey(String host,int port) {
        return String.format("%s:%d",host,port);
    }


    /**
     * group的目录
     * @param group
     * @return
     */
    public static String getServerGroupPath(String group) {
        return String.format("%s/%s",BASE_PATH,group);
    }

}
