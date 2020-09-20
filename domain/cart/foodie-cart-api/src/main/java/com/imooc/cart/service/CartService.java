package com.imooc.cart.service;

import com.imooc.pojo.ShopcartBO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: foodie-cloud
 * @description: TODO
 * @author: YYF
 * @create: 2020-09-20 21:40
 **/
@RequestMapping("cart-api")
public interface CartService {

    @PostMapping("addItem")
    boolean addItemToCart(@RequestParam("userId") String userId,
                          @RequestBody ShopcartBO shopcartBO);

    @PostMapping("removeItem")
    boolean removeItemFromCart(@RequestParam("userId") String userId,
                               @RequestParam("itemSpecId") String itemSpecId);

    @PostMapping("clearCart")
    boolean clearCart(@RequestParam("userId") String userId);
}
