package com.imooc.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @program: foodie-cloud
 * @description: TODO
 * @author: YYF
 * @create: 2020-09-20 02:27
 **/
@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
@MapperScan(basePackages = "com.imooc.order.mapper")
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
@EnableDiscoveryClient
/**
 * 由于order 服务依赖了 user ，item 服务 所以要配置扫包路径
 */
@EnableFeignClients(basePackages = {
        "com.imooc.user.service",
        "com.imooc.item.service"
})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run( OrderApplication.class, args );
    }
}
