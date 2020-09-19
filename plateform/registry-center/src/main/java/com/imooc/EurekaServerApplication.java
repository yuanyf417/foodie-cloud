package com.imooc;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @program: foodie-cloud
 * @description: EurekaServer 启动类
 * @author: YYF
 * @create: 2020-09-19 17:48
 **/
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder( EurekaServerApplication.class )
                .web( WebApplicationType.SERVLET )
                .run( args );
    }
}
