package com.imooc.order.pojo.vo;

import lombok.Data;

/**
 * @program: foodie-dev
 * @description: 二级分类VO
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class MerchantOrdersVO {

    private String merchantOrderId;
    private String merchantUserId;
    private Integer amount;
    private Integer payMethod;
    private String returnUrl;

}
