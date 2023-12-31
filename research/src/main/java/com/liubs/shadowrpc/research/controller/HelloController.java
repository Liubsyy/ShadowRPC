package com.liubs.shadowrpc.research.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liubsyy
 * @date 2023/12/19
 */
@RestController
public class HelloController {


    @GetMapping("/hello")
    public String index() {
        return "Welcome to Spring Boot!";
    }

}
