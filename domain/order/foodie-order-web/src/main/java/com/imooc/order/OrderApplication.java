package com.imooc.order;

import com.imooc.item.service.ItemService;
import com.imooc.order.fallback.itemservice.ItemCommentsFeignClient;
import com.imooc.user.service.AddressService;
import com.imooc.user.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 *  foodie-cloud
 *  TODO
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
@EnableFeignClients(
        // ItemCommentsFeignClient与ItemCommentsService URL冲突 [解决方案1]
        clients = {
                ItemCommentsFeignClient.class,
                ItemService.class,
                UserService.class,
                AddressService.class
        }
        /*basePackages = {
        "com.imooc.user.service",
        "com.imooc.item.service",
        "com.imooc.order.fallback.itemservice"
        }*/
)
@EnableCircuitBreaker
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run( OrderApplication.class, args );
    }
}
