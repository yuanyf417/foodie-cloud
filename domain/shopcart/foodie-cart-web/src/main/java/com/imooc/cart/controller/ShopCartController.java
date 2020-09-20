package com.imooc.cart.controller;

import com.imooc.controller.BaseController;
import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.pojo.ShopcartBO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: foodie-dev
 * @description: 购物车接口
 * @author: YYF
 * @create: 2019-12-19 20:56
 **/
@Api(value = "购物车接口", tags =  {"购物车相关接口"})
@RestController
@RequestMapping("shopcart")
@Slf4j
public class ShopCartController extends BaseController {

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation( value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request, HttpServletResponse response){
        if(StringUtils.isBlank( userId )) {
            return IMOOCJSONResult.errorMsg( "" );
        }
        log.info( shopcartBO.toString() );

        // 前端用户在登录的情况下，添加商品到购物车，会同时在后台端添加商品到redis
        // 需要判断当前购物车中包含已经存在的商品，做累加
        String shopcartJson = redisOperator.get( FOODIE_SHOPCART + ":" + userId );
        List<ShopcartBO> shopcartList = null;
        if (StringUtils.isNotBlank( shopcartJson )) {
            //redis 中已经有购物车了
            shopcartList = JsonUtils.jsonToList( shopcartJson, ShopcartBO.class );
            // 判断购物车中是否存在已有商品，有 count 累加
            AtomicBoolean isHaving = new AtomicBoolean( false );
            shopcartList.stream().filter( sc -> shopcartBO.getSpecId().equals( sc.getSpecId() ) )
                    .forEach( sc -> {
                        sc.setBuyCounts( String.valueOf( Integer.parseInt( sc.getBuyCounts() ) + Integer.parseInt(shopcartBO.getBuyCounts()) ) );
                        isHaving.set( true );
                    } );
            // 没有此规格的，直接加到购物中
            if (!isHaving.get()) {
                shopcartList.add( shopcartBO );
            }
        } else {
            // redis中没有购物车
            shopcartList = new ArrayList<>(  );
            shopcartList.add( shopcartBO );
        }

        redisOperator.set( FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson( shopcartList ) );

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        // 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端Redis购物车中的商品
        String shopcartJson = redisOperator.get( FOODIE_SHOPCART + ":" + userId );

        List<ShopcartBO> shopcartList = null;
        if (StringUtils.isNotBlank( shopcartJson )) {
            //如果存在购物车
            shopcartList = JsonUtils.jsonToList( shopcartJson, ShopcartBO.class );
            shopcartList.removeIf( sc-> sc.getSpecId().equals( itemSpecId ) );
        }

        redisOperator.set( FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson( shopcartList ) );
        return IMOOCJSONResult.ok();
    }

}
