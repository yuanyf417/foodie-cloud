package com.imooc.item.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @program: foodie-dev
 * @description: 商品评论VO
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class MyCommentVO {

    private String commentId;
    private String content;
    private Date createdTime;
    private String itemId;
    private String itemName;
    private String specName;
    private String itemImg;

}
