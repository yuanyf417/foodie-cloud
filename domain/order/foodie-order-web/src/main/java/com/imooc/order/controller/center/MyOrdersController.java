package com.imooc.order.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.order.pojo.vo.OrderStatusCountsVO;
import com.imooc.order.service.center.MyCommentsService;
import com.imooc.order.service.center.MyOrdersService;
import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.pojo.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * @program: foodie-dev
 * @description: 用户中心-订单相关接口
 * @author: YYF
 * @create: 2019-12-19 20:56
 **/
@Api(value = "用户中心-订单相关接口", tags = {"用户中心-订单相关接口"})
@RequestMapping("myorders")
@RestController
@Slf4j
public class MyOrdersController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "查询我的订单", notes = "查询我的订单", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = false)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "当前页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "页大小", required = false)
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank( userId )) {
            return IMOOCJSONResult.errorMsg( null );
        }

        if (page == null) {
            page = PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = myOrdersService.queryMyOrders( userId, orderStatus, page, pageSize );
        return IMOOCJSONResult.ok( pagedGridResult );
    }

    // 商家发货没有后端， 所以这个接口仅仅用于模拟
    @ApiOperation(value = "商家发货", notes = "商家发货", httpMethod = "GET" )
    @GetMapping("/deliver")
    public IMOOCJSONResult deliver(
            @ApiParam(name = "orderId", value = "订单ID", required = true,
            example = "http://localhost:8088/myorders/deliver?orderId=20050795RWZPP1KP")
            @RequestParam String orderId) {
        if (StringUtils.isBlank( orderId )) {
            return IMOOCJSONResult.errorMsg( "订单ID不能为空" );
        }

        myOrdersService.updateDeliverOrderStatus( orderId );

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "确认收货", notes = "确认收货", httpMethod = "POST" )
    @PostMapping("/confirmReceive")
    public IMOOCJSONResult confirmReceive(
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId) {

        // check 是否存在本用户的订单
        IMOOCJSONResult result = myOrdersService.checkoutUserOrder( orderId, userId );
        if (result.getStatus() != HttpStatus.OK.value()) {
            return result;
        }

        boolean b = myOrdersService.updateReceiveOrderStatus( orderId );

        if (!b) {
            IMOOCJSONResult.errorMsg("确认收货失败");
        }

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "删除订单", notes = "删除订单", httpMethod = "POST" )
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId) {

        // check 是否存在本用户的订单
        IMOOCJSONResult result = myOrdersService.checkoutUserOrder( orderId, userId );
        if (result.getStatus() != HttpStatus.OK.value()) {
            return result;
        }

        boolean b = myOrdersService.deleteOrder( userId, orderId );

        if (b) {
            return  IMOOCJSONResult.ok();
        }

        return IMOOCJSONResult.errorMsg("删除订单失败");
    }

    @ApiOperation(value = "删除订单", notes = "删除订单", httpMethod = "POST" )
    @PostMapping("/statusCounts")
    public IMOOCJSONResult statusCounts(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId) {

        if (StringUtils.isBlank( userId )) {
            IMOOCJSONResult.errorMsg("用户ID不能为空");
        }

        OrderStatusCountsVO orderStatusCounts = myOrdersService.getOrderStatusCounts( userId );

        return IMOOCJSONResult.ok(orderStatusCounts);
    }

    @ApiOperation( value = "查询订单动向", notes = "查询订单动向", httpMethod = "POST")
    @PostMapping("/trend")
    public IMOOCJSONResult trend(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "当前页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "页大小", required = false)
            @RequestParam Integer pageSize) {

        PagedGridResult myOrderTrend = myOrdersService.getOrdersTrend( userId, page, pageSize );
        return IMOOCJSONResult.ok(myOrderTrend);
    }

}
