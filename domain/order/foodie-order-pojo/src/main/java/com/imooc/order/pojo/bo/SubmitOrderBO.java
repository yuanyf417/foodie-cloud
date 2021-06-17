package com.imooc.order.pojo.bo;

import lombok.Data;
import lombok.ToString;

/**
 *  foodie-dev
 *  订单BO
 * @author: YYF
 * @create: 2020-05-07 09:46
 **/
@Data
@ToString
public class SubmitOrderBO {

    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;

}
