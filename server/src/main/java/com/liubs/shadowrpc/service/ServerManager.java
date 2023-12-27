package com.liubs.shadowrpc.service;

import com.liubs.shadowrpc.protocol.annotation.ShadowServiceHolder;
import com.liubs.shadowrpc.protocol.annotation.ShadowService;
import com.liubs.shadowrpc.protocol.serializer.SerializerManager;
import com.liubs.shadowrpc.protocol.util.AnnotationScanner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
    private Map<ServiceLookUp,ServiceTarget> allRPC = new ConcurrentHashMap<>();


    public void addRPCInterface(ServiceLookUp lookUp,ServiceTarget obj) {
        allRPC.put(lookUp,obj);
    }

    public ServiceTarget getRPC(ServiceLookUp lookUp) {
        return allRPC.get(lookUp);
    }
    


    public static ServerManager getInstance() {
        return instance;
    }

    private ServerManager() {

    }

    public ServerManager scanService(String ... packageNames){

        //序列化模块初始化
        SerializerManager.getInstance().init(packageNames);

        //初始化服务
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


                for(Method method : serviceClass.getMethods()) {

                    if(Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())){
                        continue;
                    }

                    ServiceLookUp serviceLookUp = new ServiceLookUp();
                    serviceLookUp.setServiceName(serviceAnnotation.serviceName());
                    serviceLookUp.setMethodName(method.getName());
                    serviceLookUp.setParamTypes(method.getParameterTypes());

                    ServiceTarget serviceTarget = new ServiceTarget();
                    serviceTarget.setTargetObj(o);
                    serviceTarget.setMethod(method);
                    addRPCInterface(serviceLookUp,serviceTarget);
                }

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





}
