package com.imooc.order.pojo.vo;

import com.imooc.pojo.ShopcartBO;
import lombok.Data;

import java.util.List;

/**
 *  foodie-dev
 *  二级分类VO
 * @author: YYF
 * @create: 2020-05-01 01:31
 **/
@Data
public class OrderVO {

    private String OrderId;
    private MerchantOrdersVO merchantOrdersVO;
    private List<ShopcartBO> toBeRemoveShopcartList;

}
