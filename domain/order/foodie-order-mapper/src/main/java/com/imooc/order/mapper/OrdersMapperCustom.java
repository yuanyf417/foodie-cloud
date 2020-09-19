package com.imooc.order.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.order.pojo.OrderStatus;
import com.imooc.order.pojo.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom extends MyMapper<Orders> {

    /**
     * 用户中心 - 查询我的订单
     * @param map
     * @return
     */
    List queryMyOrders(@Param("paramsMap") Map<String, Object> map);

    /**
     * 用户中心 - 查询订单状态
     * @param map
     * @return
     */
    int getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);

    /**
     * 用户中心- 查询订单动向
     * @param map
     * @return
     */
    List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);
}