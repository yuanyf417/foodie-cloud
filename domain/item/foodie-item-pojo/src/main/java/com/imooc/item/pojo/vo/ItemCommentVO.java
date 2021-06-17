package com.imooc.item.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 *  foodie-dev
 *  商品评论VO
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class ItemCommentVO {

    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickname;

}
