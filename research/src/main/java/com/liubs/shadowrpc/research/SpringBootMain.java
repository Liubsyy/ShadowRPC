package com.liubs.shadowrpc.research;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Liubsyy
 * @date 2023/12/19
 */

@SpringBootApplication
@ImportResource({"classpath:spring/bean.xml"})
public class SpringBootMain {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootMain.class, args);
    }

}
