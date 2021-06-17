package com.imooc.user.controller;

import com.imooc.controller.BaseController;
import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.pojo.ShopcartBO;
import com.imooc.user.UserApplicationProperties;
import com.imooc.user.pojo.Users;
import com.imooc.user.pojo.bo.UserBO;
import com.imooc.user.pojo.vo.UsersVO;
import com.imooc.user.service.UserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import com.imooc.utils.RedisOperator;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  foodie-dev
 *  用户登录注册
 * @author: YYF
 * @create: 2019-12-19 20:56
 **/
@RestController
@RequestMapping("passport")
@Api(value = "用户登录注册接口", tags = {"用于注册登录"})
public class PassportController extends BaseController {

    Logger logger = LoggerFactory.getLogger( this.getClass() );

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private UserApplicationProperties userApplicationProperties;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult userNameIsExist(@RequestParam String username) {

        if (StringUtils.isBlank( username )) {
            return IMOOCJSONResult.errorMsg( "用户名不能为空" );
        }

        boolean isExist = userService.queryUsernameIsExist( username );

        if (isExist) {
            return IMOOCJSONResult.errorMsg( "用户名已存在" );
        }

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult register(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {

        if (userApplicationProperties.getDisableRegistration()==null) {
            logger.info("user registration request blocked - {} ", userBO.getUsername());
            return IMOOCJSONResult.errorMsg( "当前注册用户过多,请稍候在试..." );
        }
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();

        // 0. 判断用户名密码不为空
        if (StringUtils.isBlank( username ) ||
                StringUtils.isBlank( password ) ||
                StringUtils.isBlank( confirmPwd )) {
            return IMOOCJSONResult.errorMsg( "用户名或者密码不能为空" );
        }
        // 1. 查询使用名是否存在
        boolean isExist = userService.queryUsernameIsExist( username );
        if (isExist) {
            return IMOOCJSONResult.errorMsg( "用户名已存在" );
        }
        // 2. 判断密码的长度不能少于6位
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg( "密码长度不能少于6" );
        }
        // 3.  判断两次密码是否一致
        if (!password.equals( confirmPwd )) {
            return IMOOCJSONResult.errorMsg( "两次密码输入不一致" );
        }
        // 4. 实现注册
        Users result = userService.createUser( userBO );

        //result = setNullProperty(result);

        // 生成用户token，存入redis会话
        UsersVO usersVO = conventUserVO( result );

        //3. cookie
        CookieUtils.setCookie( request, response, "user",
                JsonUtils.objectToJson( usersVO ), true );

        return IMOOCJSONResult.ok( usersVO );
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    @HystrixCommand(
            commandKey = "loginFail", // 全局唯一的标识服务，默认函数名称
            groupKey = "password", //全局服务分组，用于组织仪表盘，统计信息(默认类名)
            fallbackMethod = "loginFail", // 同一个类里，public private 都可以
            ignoreExceptions = {
                    IllegalArgumentException.class
            }, // 列表中的exception，不会触发降级
            // 线程相关的属性
            // 线程组，多个服务可以共用一个线程组
            threadPoolProperties = {
                    // 核心线程数
               @HystrixProperty( name = "coreSize", value = "20"),
                    //size > 0 , LinkedBlockingQueue -> 请求等待队列
                    // 默认-1 ， SynchronousQueue -> 不存储元素的阻塞队列
               @HystrixProperty( name = "maxQueueSize", value = "40"),
                    // 在maxQueueSize=-1的时候无效，队列没有达到maxQueueSize依然拒绝
               @HystrixProperty( name = "queueSizeRejectionThreshold", value = "15"),
                    // 统计窗口持续时间(线程池)
               @HystrixProperty( name = "metrics.rollingStats.timeInMilliseconds", value = "1024"),
                    // 窗口内桶子的数量(线程池)
               @HystrixProperty( name = "metrics.rollingStats.numBuckets", value = "18"),
            }//,
            // TODO 熔断降级的相关属性也可以配置在这里
//            commandProperties = {
//
//            }
    )
    public IMOOCJSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 0. 判断用户名密码不为空
        if (StringUtils.isBlank( username ) ||
                StringUtils.isBlank( password )) {
            return IMOOCJSONResult.errorMsg( "用户名或者密码不能为空" );
        }

        // 1. 登录
        Users result = null;
        try {
            result = userService.queryUSerForLogin( username, MD5Utils.getMD5Str( password ) );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 如果查询的信息为null
        if (result == null) {
            return IMOOCJSONResult.errorMsg( "用户名或者密码不正确" );
        }

        //2. 清除隐私信息
        // result = setNullProperty(result);

        // 生成用户token，存入redis会话
        UsersVO usersVO = conventUserVO( result );

        // 3. cookie
        CookieUtils.setCookie( request, response, "user",
                JsonUtils.objectToJson( usersVO ), true );

        // 同步购物车数据
        syncShopcartData( result.getId(), request, response );
        logger.debug( "登录成功了" );
        return IMOOCJSONResult.ok( result );

    }

    public IMOOCJSONResult loginFail( UserBO userBO, HttpServletRequest request, HttpServletResponse response, Throwable throwable) {

        return IMOOCJSONResult.errorMsg( "验证码错误(模仿12306降级)" );
    }

    /**
     * 注册登录成功后， 同步cookie和Redis中的购物车数据
     */
    // TODO 放入购物车模块
    private void syncShopcartData(String userId, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 1. redis 中无数据， 如果cookie中的购物车为空， 不作任何处理
         *                    如果cookie中购物车不为空，此时直接放入Redis
         *
         * 2. Redis 中有数据， 如果cookie中的购物车为空，此时直接放入Redis
         *                    如果cookie中购物车不为空，如果cookie中的某个商品在Redis中，以cookie为主， 把redis中的商品删除 . cookie 覆盖Redis
         * 3. 同步到Redis中去以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
         *
         */

        // 从Redis中获取购物车
        String shopcartJsonRedis = redisOperator.get( FOODIE_SHOPCART + ":" + userId );

        // 从cookie中获取购物车
        String shopcartStrCookie = CookieUtils.getCookieValue( request, FOODIE_SHOPCART, true );

        if (StringUtils.isBlank( shopcartJsonRedis )) {
            // redis 为空， cookie不为空， 直接把cookie的数据放入Redis
            if (StringUtils.isNotBlank( shopcartStrCookie )) {
                redisOperator.set( FOODIE_SHOPCART + ":" + userId, shopcartStrCookie );
            }
        } else {
            // redis不为空， cookie不为空
            if (StringUtils.isNotBlank( shopcartStrCookie )) {

                /**
                 * 1. 已经存在的， 把cookie中对应的数量， 覆盖Redis (参考京东)（--也可以做成累加）
                 * 2. 覆盖的商品的标记为待删除， 统一放入一个待删除list
                 * 3. 从cookie中清理所有的待删除list
                 * 4. 合并Redis和cookie中的数据
                 * 5. 更新到Redis和cookie中
                 */
                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList( shopcartJsonRedis, ShopcartBO.class );
                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList( shopcartStrCookie, ShopcartBO.class );
                List<ShopcartBO> pendingDeleteList = new ArrayList<>();

                // 1:
                shopcartListRedis.stream().forEach( scr -> {
                    shopcartListCookie.stream().forEach( scc -> {
                        if (scr.getSpecId().equals( scc.getSpecId() )) {
                            scr.setBuyCounts( scc.getBuyCounts() );
                            // 把cookieShopcart 放入待删除列表 ， 用于最后的删除与合并
                            pendingDeleteList.add( scc );
                        }
                    } );
                } );

                // 从现有cookie中删除对应的覆盖过的商品数据
                shopcartListCookie.removeAll( pendingDeleteList );
                // 合并两个list
                shopcartListRedis.addAll( shopcartListCookie );
                // 更新到 Redis ， cookie
                redisOperator.set( FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson( shopcartListRedis ) );
                CookieUtils.setCookie( request, response, FOODIE_SHOPCART, JsonUtils.objectToJson( shopcartListRedis ), true );

            } else {
                //cookie 为空的话，
                CookieUtils.setCookie( request, response, FOODIE_SHOPCART, shopcartJsonRedis, true );
            }
        }

    }


    @ApiOperation(value = "用户注销", notes = "用户注销", httpMethod = "POST")
    @PostMapping("logout")
    public IMOOCJSONResult logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        //清除用户相关的cookie信息
        CookieUtils.deleteCookie( request, response, "user" );
        // 用户退出登录，需清空购物车
        CookieUtils.deleteCookie( request, response, FOODIE_SHOPCART );
        // 分布式会话中，需清空用户数据
        redisOperator.del( REDIS_USER_TOKEN + ":" + userId );
        logger.info( "用户注销成功：user = {}", userId );
        return IMOOCJSONResult.ok();
    }


    /**
     * 清除隐私信息
     *
     * @param result userResult
     * @return Users
     */
    private Users setNullProperty(Users result) {
        result.setPassword( null );
        result.setMobile( null );
        result.setEmail( null );
        result.setCreatedTime( null );
        result.setUpdatedTime( null );
        result.setBirthday( null );

        return result;
    }

    /**
     * UsersVO 转换， 并保存至Redis
     *
     * @param result
     * @return
     */
    private UsersVO conventUserVO(Users result) {

        String uniqueToke = UUID.randomUUID().toString().trim();
        redisOperator.set( REDIS_USER_TOKEN + ":" + result.getId(), uniqueToke );

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties( result, usersVO );
        usersVO.setUserUniqueToken( uniqueToke );
        return usersVO;
    }


}
