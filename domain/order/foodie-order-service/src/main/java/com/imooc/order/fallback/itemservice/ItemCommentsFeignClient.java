package com.imooc.order.fallback.itemservice;

import com.imooc.item.service.ItemCommentsService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 由于集成ItemCommentsService的同时，导致 @RequestMapping("item-comments-api") 和 @XXXMapping注解也从原接口继承而来
 * 因此不能同时配置两个完全一样的访问路径，否则报错 [IllegalStateException: Ambiguous mapping]
 *
 * 解决方案（避免spring加载两个）：
 * 1）
 *  在启动类扫包的时候，不要把原始Feign接口扫描出来
 *  具体做法： 可以使用EnableFeignClients注解的clients属性，只加载需要的Feign接口
 *      优点： 服务提供者和服务调用者都不需要额外的配置
 *      缺点： 启动的时候配置麻烦一点，需要指定加载每一个用到的接口
 * 2）
 *  原始的Feign接口不要定义requestMapping注解
 *      优点： 启动的时候直接扫包即可，不用加载指定的接口
 *      缺点：
 *          a, 服务提供者要额外的配置路径访问的注解
 *          b, 任何情况下，即时不需要在调用端定义fallback类，服务调用者都要什么一个
 * 3）
 *  原始的Feign接口不要定义FeignClients注解
 *      优点： 启动的时候直接扫包即可，不用加载指定的接口
 *      缺点： 任何情况下，服务调用者都要声明一个@FeignClient接口
 *
 * @program: foodie-cloud
 * @description: TODO
 * @author: YYF
 * @create: 2020-10-06 02:46
 **/

// 两种方式
//@FeignClient(value = "foodie-item-service", fallback = ItemCommentsFallback.class )
@FeignClient(value = "foodie-item-service", fallbackFactory = ItemCommentFallbackFactory.class )
public interface ItemCommentsFeignClient extends ItemCommentsService {


}
