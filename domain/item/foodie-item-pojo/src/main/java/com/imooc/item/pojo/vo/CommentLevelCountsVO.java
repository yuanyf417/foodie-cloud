package com.imooc.item.pojo.vo;

import lombok.Data;

/**
 *  foodie-dev
 *  商品评价数量VO
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class CommentLevelCountsVO {

    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;

}
