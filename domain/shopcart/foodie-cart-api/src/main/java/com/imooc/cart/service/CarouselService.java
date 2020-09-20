package com.imooc.cart.service;

import com.imooc.cart.pojo.Carousel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: foodie-dev
 * @description: 轮播图
 * @author: YYF
 * @create: 2019-12-25 22:31
 **/
@RequestMapping("cart-carousel-api")
public interface CarouselService {

    /**
     * 查询所有轮播图
     * @param isShow
     * @return
     */
    @GetMapping("list")
    List<Carousel> queryAll(@RequestParam("isShow") Integer isShow);

}
