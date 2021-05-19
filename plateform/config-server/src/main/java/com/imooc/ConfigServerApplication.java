package com.imooc;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author 秋风无赖.
 * date 2021/5/19 00:20.
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder( ConfigServerApplication.class )
                .web( WebApplicationType.SERVLET )
                .run( args );
    }
}
