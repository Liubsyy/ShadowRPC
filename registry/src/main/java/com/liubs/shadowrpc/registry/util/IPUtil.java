package com.liubs.shadowrpc.registry.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Liubsyy
 * @date 2023/12/14
 */
public class IPUtil {

    private static String ipAddress = getLocalIp();

    public static String getLocalIp(){
        InetAddress address = null;
        String ip = null;
        try {
            address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
