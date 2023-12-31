package com.liubs.shadowrpc.research.dubbo;

/**
 * @author Liubsyy
 * @date 2023/12/19
 */
public class GreetingServiceImpl2 implements GreetingService {
    public String sayHello(String name) {
        return "Hello2, " + name;
    }
}
