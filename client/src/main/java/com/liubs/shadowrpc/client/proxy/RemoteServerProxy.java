package com.liubs.shadowrpc.client.proxy;

import com.liubs.shadowrpc.base.annotation.ShadowInterface;
import com.liubs.shadowrpc.client.connection.IConnection;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Liubsyy
 * @date 2023/12/3 11:29 PM
 **/
public class RemoteServerProxy {

    /**
     * service(集群+服务) => 代理对象
     */
    private static Map<String,Object> proxyServiceCache = new ConcurrentHashMap<>();

    public static boolean hasLoaded(String service) {
        return proxyServiceCache.containsKey(service);
    }

    /**
     *
     * @param connection
     * @param serviceStub
     * @param service shadowrpc://group/serviceName
     * @return
     * @param <T>
     */
    public static <T> T create(IConnection connection, Class<T> serviceStub, final String service) {

        Object proxyInstance = proxyServiceCache.get(service);
        if(null == proxyInstance) {
            synchronized (serviceStub) {
                proxyInstance = proxyServiceCache.get(service);
                if(null == proxyInstance) {
                    ShadowInterface shadowInterface = serviceStub.getAnnotation(ShadowInterface.class);
                    if(null == shadowInterface) {
                        throw new RuntimeException("服务未找到 @shadowInterface注解");
                    }

                    String[] serviceArr = service.replace("shadowrpc://","").split("/");
                    if(serviceArr.length < 2) {
                        throw new IllegalArgumentException("service参数不符合规范");
                    }
                    String group = serviceArr[0];
                    String serviceName = serviceArr[1];

                    proxyInstance = Proxy.newProxyInstance(
                            serviceStub.getClassLoader(),
                            new Class<?>[]{serviceStub},
                            new RemoteHandler(connection,serviceStub,group,serviceName)
                    );
                }
            }
        }
        return (T)proxyInstance;
    }

}
