package com.liubs.shadowrpc.service;

import com.liubs.shadowrpc.protocol.annotation.ShadowServiceHolder;
import com.liubs.shadowrpc.protocol.annotation.ShadowService;
import com.liubs.shadowrpc.protocol.util.AnnotationScanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:49 PM
 */
public class ServerManager {

    private static ServerManager instance = new ServerManager();


    private Server server;

    //所有服务
    private Map<String,Object> allRPC = new ConcurrentHashMap<>();


    public void addRPCInterface(String serviceName,Object obj) {
        allRPC.put(serviceName,obj);
    }

    public Object getRPC(String serviceName) {
        return allRPC.get(serviceName);
    }
    


    public static ServerManager getInstance() {
        return instance;
    }

    private ServerManager() {

    }

    public ServerManager scanService(String ... packageNames){

        List<ShadowServiceHolder<ShadowService>> shadowServices = new ArrayList<>();

        for(String packageName : packageNames) {
            try {
                shadowServices.addAll(AnnotationScanner.scanAnnotations(packageName, ShadowService.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(ShadowServiceHolder<ShadowService> ShadowServiceHolder : shadowServices) {
            ShadowService serviceAnnotation = ShadowServiceHolder.getAnnotation();
            Class<?> serviceClass = ShadowServiceHolder.getClassz();
            try {
                Object o = serviceClass.newInstance();
                addRPCInterface(serviceAnnotation.serviceName(),o);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }




    /**
     * @param port
     * @return
     */
    public Server startServer(int port) {
        server = new Server("",port);
        server.start();
        return server;
    }

    public Server startServer(String zkUrl,int port) {
        server = new Server("",port);
        server.zkUrl(zkUrl);
        server.start();
        return server;
    }




    public Object getService(String serviceName) {
        return getRPC(serviceName);
    }

}
