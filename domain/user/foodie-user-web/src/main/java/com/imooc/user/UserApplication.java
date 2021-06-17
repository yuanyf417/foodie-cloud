package com.imooc.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import tk.mybatis.spring.annotation.MapperScan;

/**
 *  foodie-cloud
 *  TODO
 * @author: YYF
 * @create: 2020-09-19 23:46
 **/
@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
@MapperScan(basePackages = "com.imooc.user.mapper")
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
@EnableDiscoveryClient
@EnableCircuitBreaker
// TODO feign 注解
@Profile("dev")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run( UserApplication.class, args );
    }
}
