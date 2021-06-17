package com.imooc.order.pojo.bo;

import com.imooc.pojo.ShopcartBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *  foodie-cloud
 *  下单
 * @author: YYF
 * @create: 2020-09-20 01:10
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderBO {

    private SubmitOrderBO order;

    private List<ShopcartBO> items;
}
