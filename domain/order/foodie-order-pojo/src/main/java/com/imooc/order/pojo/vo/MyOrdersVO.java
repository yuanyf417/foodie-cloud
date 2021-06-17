package com.imooc.order.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *  foodie-dev
 *  我的订单
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class MyOrdersVO {

    private String orderId;
    private Date createdTime;
    private Integer payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer isComment;
    private Integer orderStatus;
    private List<MySubOrderItemVO> subOrderItemList;


}
