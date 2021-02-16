package com.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.test")
@SpringBootApplication
//@ComponentScan({
//        "com.example.servingwebcontent"
//})
@MapperScan({
        "com.test.mapper"
})
@EnableDiscoveryClient
public class myStartingApplication {

    public static void main(String[] args) {
        SpringApplication.run(myStartingApplication.class, args);
    }

}
