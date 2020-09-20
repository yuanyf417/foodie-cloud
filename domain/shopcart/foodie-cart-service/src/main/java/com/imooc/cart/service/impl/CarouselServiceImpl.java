package com.imooc.cart.service.impl;

import com.imooc.cart.mapper.CarouselMapper;
import com.imooc.cart.pojo.Carousel;
import com.imooc.cart.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @program: foodie-dev
 * @description: TODO
 * @author: YYF
 * @create: 2019-12-25 22:33
 **/
@RestController
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> queryAll(Integer isShow) {

        Example example = new Example( Carousel.class );
        example.orderBy( "sort" ).desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo( "isShow" ,isShow );

        List<Carousel> carousels = carouselMapper.selectByExample( example );
        return carousels;
    }
}
