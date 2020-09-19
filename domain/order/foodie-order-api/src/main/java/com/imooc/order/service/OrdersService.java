package com.imooc.order.service;


import com.imooc.order.pojo.OrderStatus;
import com.imooc.order.pojo.bo.PlaceOrderBO;
import com.imooc.order.pojo.vo.OrderVO;
import org.springframework.web.bind.annotation.*;

/**
 * @program: foodie-dev
 * @description: 订单
 * @author: YYF
 * @create: 2019-12-25 22:31
 **/
@RequestMapping("order-api")
public interface OrdersService {

    /**
     * 创建订单
     * @param orderBO
     * @return
     */
    @PostMapping("placeOrder")
    OrderVO createOrder(@RequestBody PlaceOrderBO orderBO);

    /**
     * 更新订单状态
     * @param orderId
     * @param orderStatus
     */
    @PostMapping("updateStatus")
    void updateOrderStatus(@RequestParam("orderId") String orderId,
                           @RequestParam("orderStatus") Integer orderStatus);

    /**
     * 查询订单信息
     * @param orderId
     */
    @GetMapping("orderStatus")
    OrderStatus queryOrderStatusInfo(@RequestParam("orderId") String orderId);

    /**
     * 关闭超时订单
     */
    @PostMapping("closePendingOrders")
    void closeOrder();


}
