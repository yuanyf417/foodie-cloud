package com.imooc.order.pojo.bo.center;

import lombok.Data;
import lombok.ToString;

/**
 * @program: foodie-dev
 * @description: 用户中心-发表评论BO
 * @author: YYF
 * @create: 2020-05-09 16:24
 **/
@Data
@ToString
public class OrderItemsCommentBO {

    private String commentId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private String commentLevel;
    private String content;

}
