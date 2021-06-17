package com.imooc.order.pojo.vo;

import lombok.Data;

/**
 *  foodie-dev
 *  我的订单子类
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class MySubOrderItemVO {

    private String  itemId;
    private String  itemName;
    private String  itemImg;
    private String  itemSpecId;
    private String  itemSpecName;
    private String  buyCounts;
    private Integer  price;

}
