package com.imooc.cart.pojo.vo;

import lombok.Data;

/**
 * @program: foodie-dev
 * @description: 首页子分类
 * @author: YYF
 * @create: 2020-05-01 01:33
 **/
@Data
public class SubCategoryVO {

    private Integer subId;
    private String subName;
    private String subType;
    private String subFatherId;

}
