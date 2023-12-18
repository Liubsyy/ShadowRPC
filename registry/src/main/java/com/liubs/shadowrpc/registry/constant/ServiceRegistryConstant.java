package com.liubs.shadowrpc.registry.constant;

import com.liubs.shadowrpc.registry.util.IPUtil;

/**
 * @author Liubsyy
 * @date 2023/12/18 11:35 PM
 **/
public class ServiceRegistryConstant {
    public static final String BASE_PATH = "/shadowrpc/services";


    public static String getServerNodeStr(int port) {
        return BASE_PATH+"/"+String.format(IPUtil.getLocalIp()+":"+port);
    }

}
