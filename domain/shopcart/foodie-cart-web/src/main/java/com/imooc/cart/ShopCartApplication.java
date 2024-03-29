package com.imooc.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 *  foodie-cloud
 *  TODO
 * @author: YYF
 * @create: 2020-09-20 13:22
 **/
@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
@MapperScan(basePackages = "com.imooc.cart.mapper")
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
@EnableDiscoveryClient
// TODO feign 注解
@EnableCircuitBreaker
public class ShopCartApplication {

    public static void main(String[] args) {
        SpringApplication.run( ShopCartApplication.class, args );
    }

}
