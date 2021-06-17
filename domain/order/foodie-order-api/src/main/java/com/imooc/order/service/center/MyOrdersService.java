package com.imooc.order.service.center;


import com.imooc.order.pojo.Orders;
import com.imooc.order.pojo.vo.OrderStatusCountsVO;
import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.pojo.PagedGridResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 *  foodie-dev
 *  订单
 * @author: YYF
 * @create: 2019-12-25 22:31
 **/
@FeignClient("foodie-order-service")
@RequestMapping("myorder-api")
public interface MyOrdersService {

    /**
     * 查询我的订单
     * @param userId
     * @param orderStatus
     * @return
     */
    @GetMapping("order/query")
    PagedGridResult queryMyOrders(@RequestParam("userId") String userId,
                                  @RequestParam("orderStatus") Integer orderStatus,
                                  @RequestParam(value = "page", required = false) Integer page,
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 查询我的订单
     * @param userId
     * @param orderId
     * @return
     */
    @GetMapping("order/details")
    Orders queryMyOrder(@RequestParam("userId") String userId,
                        @RequestParam("orderId") String orderId);


    /**
     * 根据订单ID，更新订单状态 已发货
     * @param orderId
     */
    @PostMapping("order/delivered")
    void updateDeliverOrderStatus(@RequestParam("orderId") String orderId);

    /**
     * 确认收货
     * @param orderId
     * @return
     */
    @PostMapping("order/received")
    boolean updateReceiveOrderStatus(@RequestParam("orderId") String orderId);

    /**
     * 删除订单-逻辑删除
     * @param userId
     * @param orderId
     * @return
     */
    @DeleteMapping("order")
    boolean deleteOrder(@RequestParam("userId") String userId,
                        @RequestParam("orderId") String orderId);

    /**
     * 查询用户订单数
     * @param userId
     * @return
     */
    @GetMapping("order/counts")
    OrderStatusCountsVO getOrderStatusCounts(@RequestParam("userId") String userId);

    /**
     * 获取分页的订单动向
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("order/trend")
    PagedGridResult getOrdersTrend(@RequestParam("userId") String userId,
                                   @RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /**
     * 检查订单情况
     * @param orderId
     * @param userId
     * @return
     */
    @GetMapping("checkUserOrder")
    IMOOCJSONResult checkoutUserOrder(@RequestParam("orderId") String orderId,
                                      @RequestParam("userId") String userId);


}
