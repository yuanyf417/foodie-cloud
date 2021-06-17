package com.imooc.cart.pojo.vo;

import lombok.Data;

/**
 *  foodie-dev
 *  二级分类VO
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class ShopcartVO {

    /**
     * 商品ID
     */
    private String itemId;

    /**
     * 商品主图
     */
    private String itemImgUrl;
    /**
     * 商品名称
     */
    private String itemName;
    /**
     * 规格ID
     */
    private String specId;
    /**
     * 规格名称
     */
    private String specName;
    /**
     * 打折价
     */
    private Integer priceDiscount;
    /**
     * 正常价
     */
    private Integer priceNormal;
}
