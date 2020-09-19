package com.imooc.order.controller;


import com.imooc.controller.BaseController;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;

import com.imooc.order.pojo.OrderStatus;
import com.imooc.order.pojo.bo.PlaceOrderBO;
import com.imooc.order.pojo.bo.SubmitOrderBO;
import com.imooc.order.pojo.vo.MerchantOrdersVO;
import com.imooc.order.pojo.vo.OrderVO;
import com.imooc.order.service.OrdersService;
import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.pojo.ShopcartBO;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @program: foodie-dev
 * @description: 订单相关的接口
 * @author: YYF
 * @create: 2019-12-19 20:56
 **/
@Api(value = "订单相关的接口", tags = {"订单相关的接口"})
@RequestMapping("orders")
@RestController
@Slf4j
public class OrdersController extends BaseController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation( value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(
            @ApiParam(name = "submitOrderBO", value = "订单BO", required = true)
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
            && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return IMOOCJSONResult.errorMsg( "支付方式不支持！" );
        }
        log.info( submitOrderBO.toString() );
        String shopcartJson = redisOperator.get( FOODIE_SHOPCART + ":" + submitOrderBO.getUserId() );
        if (StringUtils.isBlank( shopcartJson )) {
            return IMOOCJSONResult.errorMsg( "购物车数据异常" );
        }

        List<ShopcartBO> shopcartList = JsonUtils.jsonToList( shopcartJson, ShopcartBO.class );

        //1. 创建订单
        PlaceOrderBO placeOrderBO = new PlaceOrderBO(submitOrderBO, shopcartList);

        OrderVO orderVO = ordersService.createOrder( placeOrderBO );
        String orderId = orderVO.getOrderId();

        //2. 创建订单后，移除购物车中已结算的商品

        shopcartList.removeAll( orderVO.getToBeRemoveShopcartList() );
        // 整合Redis， 完善购物车中的已结算商品清除，并且同步到cookie中
        redisOperator.set( FOODIE_SHOPCART + ":" + submitOrderBO.getUserId(), JsonUtils.objectToJson( shopcartList ) );
        CookieUtils.setCookie( httpServletRequest,httpServletResponse,FOODIE_SHOPCART, JsonUtils.objectToJson( shopcartList ),true );

        //3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl( payReturnUrl );

        merchantOrdersVO.setAmount( 1 );

        HttpHeaders headers = new HttpHeaders(  );
        headers.setContentType( MediaType.APPLICATION_JSON );
        headers.add( "imoocUserId","imooc" );
        headers.add( "password","imooc" );



        // TODO 临时写的一个调用支付中心的rest ，正常情况应该是发post请求的

        // TODO 临时注释的POST请求支付中心
        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>( merchantOrdersVO, headers );
        ResponseEntity<IMOOCJSONResult> resultResponseEntity = restTemplate.postForEntity( paymentUrl, entity, IMOOCJSONResult.class );
        IMOOCJSONResult paymentResult = resultResponseEntity.getBody();
        if(paymentResult.getStatus() != 200) {
            return IMOOCJSONResult.errorMsg( "支付中心订单创建失败，请联系管理员！" );
        }

        // TODO 模拟支付
        if(orderId.substring(orderId.length()-1).matches("^[0-9]*$")
        || orderId.substring(orderId.length()-1).matches("^[A-H]*$")){
            async( orderId);
        }

        return IMOOCJSONResult.ok( orderId );
    }

    /**
     * 异步执行模拟付款成功回调 --> 发货
     * @param orderId
     */
    @Async
    public void async(String orderId) {
        try {
            Thread.sleep( 3000 );
        } catch (InterruptedException e) {

        }
        Integer result =this.notifyMerchantOrderPaid( orderId );
        asyncDeliver( orderId);
        if (result == 1 ) {
            log.info( "模拟付款成功！" );
        }
    }

    @Async
    public void asyncDeliver(String orderId) {
        String dubOrderId = orderId.substring(orderId.length()-1);

        if(dubOrderId.matches("^[0-9]*$")){
            try {
                Thread.sleep( 10000 );
            } catch (InterruptedException e) {

            }
           //myOrdersService.updateDeliverOrderStatus( orderId );
           //log.info( "模拟发货成功！" );
        }
    }

    @ApiOperation( value = "支付中心回调", notes = "支付中心回调", httpMethod = "POST")
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(
           String merchantOrderId) {

        ordersService.updateOrderStatus( merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type );
        return HttpStatus.OK.value();
    }

    @ApiOperation( value = "查询订单信息", notes = "查询订单信息", httpMethod = "POST")
    @PostMapping("/getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo( String orderId ) {

        OrderStatus orderStatus = ordersService.queryOrderStatusInfo( orderId );
        return IMOOCJSONResult.ok(orderStatus);
    }



}
