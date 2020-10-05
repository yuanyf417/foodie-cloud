package com.imooc.order.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.enums.YesOrNo;

import com.imooc.order.fallback.itemservice.ItemCommentsFeignClient;
import com.imooc.order.pojo.OrderItems;
import com.imooc.order.pojo.Orders;
import com.imooc.order.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.order.service.center.MyCommentsService;
import com.imooc.order.service.center.MyOrdersService;
import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.pojo.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @program: foodie-dev
 * @description: 我的评价相关接口
 * @author: YYF
 * @create: 2019-12-19 20:56
 **/
@Api(value = "用户中心-评价相关接口", tags = {"用户中心-评价相关接口"})
@RequestMapping("mycomments")
@RestController
@Slf4j
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    @Autowired
    //private ItemCommentsService itemCommentsService;
    private ItemCommentsFeignClient itemCommentsService;

    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "追加订单评论", notes = "追加订单评论", httpMethod = "POST")
    @PostMapping("/pending")
    public IMOOCJSONResult pending(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId) {


        // 判断用户是否和订单关联
        IMOOCJSONResult result = myOrdersService.checkoutUserOrder( orderId, userId );
        if (result.getStatus() != HttpStatus.OK.value()) {
            return result;
        }

        // 判断订单是否已经评价
        Orders resultData = (Orders) result.getData();

        if (resultData.getIsComment().equals( YesOrNo.YES.type )) {
            return IMOOCJSONResult.errorMsg( "该笔订单已经评价过了！" );
        }

        List<OrderItems> orderItems = myCommentsService.queryPendingComment( orderId );

        return IMOOCJSONResult.ok( orderItems );
    }

    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public IMOOCJSONResult saveList(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {


        System.out.println(commentList);

        // 判断用户是否和订单关联
        IMOOCJSONResult result = myOrdersService.checkoutUserOrder( orderId, userId );
        if (result.getStatus() != HttpStatus.OK.value()) {
            return result;
        }

        // 判断评价内容不能为空
        if (CollectionUtils.isEmpty( commentList )) {
            return IMOOCJSONResult.errorMsg( "评论内容不能为空" );
        }

        myCommentsService.saveComment( orderId,userId, commentList );
        return IMOOCJSONResult.ok(  );
    }

    @ApiOperation(value = "查询评论列表", notes = "查询评论列表", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "当前页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "页大小", required = false)
            @RequestParam Integer pageSize) {

        if (page == null) {
            page = PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult orderItems = itemCommentsService.queryMyComments( userId, page ,pageSize );

        return IMOOCJSONResult.ok( orderItems );
    }


}
