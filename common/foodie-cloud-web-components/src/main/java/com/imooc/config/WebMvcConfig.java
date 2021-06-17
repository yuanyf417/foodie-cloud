package com.imooc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  foodie-dev
 *  TODO
 * @author: YYF
 * @create: 2019-12-21 14:11
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    /**
     *  实现静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler( "/**" )
                // 映射swagger2
                .addResourceLocations( "classpath:/META-INF/resources/" )
                // 映射本地静态资源
                .addResourceLocations( "file:/imoocWorkspace/images/" );
    }

    /**
     * 注入RestTemplate
     *
     * @param restTemplateBuilder
     * @return
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.build();
    }

   /* @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    *//**
     * 添加拦截器
     *
     * @param registry
     *//*
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor( userTokenInterceptor() )
                //.addPathPatterns( "/hello" )
                .addPathPatterns( "/shopcart/add" )
                .addPathPatterns( "/shopcart/del" )
                .addPathPatterns( "/address/list" )
                .addPathPatterns( "/address/add" )
                .addPathPatterns( "/address/update" )
                .addPathPatterns( "/address/setDefalut" )
                .addPathPatterns( "/address/delete" )
                .addPathPatterns( "/orders/*" )
                .addPathPatterns( "/center/*" )
                .addPathPatterns( "/userInfo/*" )
                .addPathPatterns( "/myorders/*" )
                .addPathPatterns( "/mycomments/*" )
                .excludePathPatterns( "/myorders/deliver" )
                .excludePathPatterns( "/orders/notifyMerchantOrderPaid" )
        ;
        WebMvcConfigurer.super.addInterceptors( registry );
    }*/
}
