package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *  foodie-dev
 *  TODO
 * @author: YYF
 * @create: 2019-12-21 14:11
 **/
@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    @Bean
    public CorsFilter corsFilter(){
       /* // 1. 添加cors配置信息
        CorsConfiguration configuration = new CorsConfiguration(  );
        configuration.addAllowedOrigin( "http://localhost:8080" );

        // 设置是否发送cookie信息
        configuration.setAllowCredentials(true);
        //设置允许请求的方式
        configuration.addAllowedMethod( "*" );

        //设置允许的header
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**",configuration );

        return new CorsFilter( source );*/

        // 1. 添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://192.168.1.102:8080");
        config.addAllowedOrigin("http://192.168.1.102");
        config.addAllowedOrigin("http://www.shop.z.mukewang.com:8080");
        config.addAllowedOrigin("http://www.shop.z.mukewang.com");
        config.addAllowedOrigin("http://www.shop.z.yuanyanfei.cn:8081");
        config.addAllowedOrigin("http://shop.z.yuanyanfei.cn:8081");
        config.addAllowedOrigin("http://center.z.yuanyanfei.cn:8081");
        config.addAllowedOrigin("http://center.z.yuanyanfei.cn:8088");
        config.addAllowedOrigin("http://www.shop.z.yuanyanfei.cn:8088");
        config.addAllowedOrigin("http://shop.z.yuanyanfei.cn:8088");
        config.addAllowedOrigin("http://www.shop.z.yuanyanfei.cn");
        config.addAllowedOrigin("http://payment.t.mukewang.com");
        config.addAllowedOrigin("http://139.129.231.161:8080");

        // 设置是否发送cookie信息
        config.setAllowCredentials(true);

        //设置允许请求的方式
        config.addAllowedMethod("*");

        //设置允许的header
        config.addAllowedHeader("*");

        // 2. 添加映射
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        // 3. 返回重新定义好的corsSource

        return new CorsFilter(corsSource);
    }
}
