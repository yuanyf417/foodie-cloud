package com.imooc.order.service.center;


import com.imooc.order.pojo.OrderItems;
import com.imooc.order.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.order.pojo.vo.OrderStatusCountsVO;
import com.imooc.pojo.PagedGridResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: foodie-dev
 * @description: 评论
 * @author: YYF
 * @create: 2019-12-25 22:31
 **/
@RequestMapping("order-comments-api")
public interface MyCommentsService {

    /**
     * 查询评论
     * @param orderId
     * @return
     */
    @GetMapping("orderItems")
    List<OrderItems> queryPendingComment(@RequestParam("orderId") String orderId);

    /**
     * 保存评论
     * @param orderId
     * @param userId
     * @param commentBOList
     */
    @PostMapping("saveOrderComments")
    void saveComment(@RequestParam("orderId") String orderId,
                     @RequestParam("userId") String userId,
                     @RequestBody List<OrderItemsCommentBO> commentBOList);

    // TODO 移到了itemCommentsService 里
   /* *//**
     * 查询我的评论
     * @param userId
     * @param page
     * @param pageSize
     * @return
     *//*
    PagedGridResult queryMyComments(@RequestParam("orderId") String userId,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 查询我的订单状态数量
     * @param userId
     */
   // OrderStatusCountsVO getOrderStatusCounts(@RequestParam("userId") String userId);

    /**
     * 查询我的订单动向
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
  //  PagedGridResult getMyOrderTrend(@RequestParam("userId") String userId,
  //                                  @RequestParam(value = "page", required = false) Integer page,
  //                                  @RequestParam(value = "pageSize", required = false) Integer pageSize);
}
