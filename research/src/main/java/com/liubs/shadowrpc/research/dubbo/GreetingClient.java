package com.liubs.shadowrpc.research.dubbo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Liubsyy
 * @date 2023/12/19
 */
public class GreetingClient {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/dubbo-consumer.xml");
        GreetingService greetingService = (GreetingService) context.getBean("greetingService");

        String greeting = greetingService.sayHello("World");
        System.out.println(greeting);

        GreetingService greetingService2 = (GreetingService) context.getBean("greetingService2");
        String greeting2 = greetingService2.sayHello("World");
        System.out.println(greeting2);
    }
}
