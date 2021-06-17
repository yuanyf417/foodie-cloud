package com.imooc.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  foodie-dev
 *  购物车BO
 * @author: YYF
 * @create: 2020-05-06 18:05
 **/
@Data
@NoArgsConstructor
@ApiModel("购物车BO")
public class ShopcartBO {

    @ApiModelProperty(name = "商品id")
    private String itemId;
    @ApiModelProperty(name = "商品图片URL")
    private String itemImgUrl;
    @ApiModelProperty(name = "商品名称")
    private String itemName;
    @ApiModelProperty(name = "商品规格id")
    private String specId;
    @ApiModelProperty(name = "商品规格名称")
    private String specName;
    @ApiModelProperty(name = "购买数量")
    private String buyCounts;
    @ApiModelProperty(name = "折扣")
    private Integer priceDiscount;
    @ApiModelProperty(name = "价格")
    private String priceNormal;

    public ShopcartBO(String itemId, String itemImgUrl, String itemName, String specId, String specName, String buyCounts, Integer priceDiscount, String priceNormal) {
        this.itemId = itemId;
        this.itemImgUrl = itemImgUrl;
        this.itemName = itemName;
        this.specId = specId;
        this.specName = specName;
        this.buyCounts = buyCounts;
        this.priceDiscount = priceDiscount;
        this.priceNormal = priceNormal;
    }
}
