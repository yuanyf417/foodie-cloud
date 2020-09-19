package com.imooc.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @program: foodie-dev
 * @description: 基类Controller
 * @author: YYF
 * @create: 2019-12-19 20:56
 **/
@RestController
public class BaseController {

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 10;
    public static final Integer PAGE = 1;

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    // 支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    // String paymentUrl = "http://localhost:8089/payment/createMerchantOrder";
    // String paymentUrl = "http://localhost:8089/payment/createMerchantOrder";

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    //String payReturnUrl = "http://5vyqyi.natappfree.cc/orders/notifyMerchantOrderPaid";
    String payReturnUrl = "http://api.z.yuanyanfei.cn:8081/orders/notifyMerchantOrderPaid";

    // 用户上传头像位置
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "imoocWorkspace" + File.separator + "images" + File.separator + "foodie" + File.separator + "faces";




    // FIXME 下面的逻辑移植到订单中心
//    public IMOOCJSONResult checkoutUserOrder(String orderId, String userId) {
//        Orders orders = myOrdersService.queryMyOrder( userId, orderId );
//
//        if (orders == null) {
//            return Result.errorMsg( "订单不存在！" );
//        }
//        return Result.ok( orders );
//    }






//    /**
//     * UsersVO 转换， 并保存至Redis
//     *
//     * @param result
//     * @return
//     */
//    public UsersVO conventUserVO(Users result) {
//
//        String uniqueToke = UUID.randomUUID().toString().trim();
//        redisOperator.set( REDIS_USER_TOKEN + ":" + result.getId(), uniqueToke );
//
//        UsersVO usersVO = new UsersVO();
//        BeanUtils.copyProperties( result, usersVO );
//        usersVO.setUserUniqueToken( uniqueToke );
//        return usersVO;
//    }

}
