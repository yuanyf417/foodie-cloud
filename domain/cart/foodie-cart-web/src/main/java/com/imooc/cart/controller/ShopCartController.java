package com.imooc.cart.controller;

import com.imooc.cart.service.CartService;
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

    @Autowired
    private CartService cartService;

    @ApiOperation( value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request, HttpServletResponse response){

        if(StringUtils.isBlank( userId )) {
            IMOOCJSONResult.errorMap("用户不存在！");
        }
        log.info( shopcartBO.toString() );

        boolean b = cartService.addItemToCart( userId, shopcartBO );

        if(!b) {
            IMOOCJSONResult.errorMap("添加商品到购物车失败");
        }
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

        boolean b = cartService.removeItemFromCart( userId, itemSpecId );

        if (!b) {
            return IMOOCJSONResult.errorMsg("从购物车中删除商品失败");
        }
        return IMOOCJSONResult.ok();
    }

    // TODO  1：购物车清空
    //       2：加减商品数量
    //       +1 -1 -1 = 0  ==>  -1 -1 +1 ( 如何保证前端请求顺序执行

    @ApiOperation(value = "清空购物车", notes = "清空购物车", httpMethod = "POST")
    @PostMapping("/clear")
    public IMOOCJSONResult clear(String userId) {


        return IMOOCJSONResult.ok();
    }



}
