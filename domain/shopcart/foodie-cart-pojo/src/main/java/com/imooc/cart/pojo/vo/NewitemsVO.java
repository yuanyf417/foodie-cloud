package com.imooc.cart.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 *  foodie-dev
 *  首页最新商品VO
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class NewitemsVO {

    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;
    private List<SimpleItemVO> simpleItemList;

}
