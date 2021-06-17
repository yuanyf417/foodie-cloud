package com.imooc.order.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.item.pojo.Items;
import com.imooc.item.pojo.ItemsSpec;
import com.imooc.item.service.ItemService;
import com.imooc.order.mapper.OrderItemsMapper;
import com.imooc.order.mapper.OrderStatusMapper;
import com.imooc.order.mapper.OrdersMapper;
import com.imooc.order.pojo.OrderItems;
import com.imooc.order.pojo.OrderStatus;
import com.imooc.order.pojo.Orders;
import com.imooc.order.pojo.bo.PlaceOrderBO;
import com.imooc.order.pojo.bo.SubmitOrderBO;
import com.imooc.order.pojo.vo.MerchantOrdersVO;
import com.imooc.order.pojo.vo.OrderVO;
import com.imooc.order.service.OrdersService;
import com.imooc.pojo.ShopcartBO;
import com.imooc.user.pojo.UserAddress;
import com.imooc.user.service.AddressService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *  foodie-dev
 *  订单服务
 * @author: YYF
 * @create: 2020-05-07 10:04
 **/
@RestController
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;



    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private Sid sid;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

//    @Autowired
//    private LoadBalancerClient client;
//    @Autowired
//    private RestTemplate restTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(PlaceOrderBO orderBO) {

        SubmitOrderBO submitOrderBO = orderBO.getOrder();
        List<ShopcartBO> shopcartList = orderBO.getItems();

        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();

        //包邮费用设置 0
        Integer postAmount = 0;

        String orderId = sid.nextShort();

        //1. 新订单数据保存
        Orders newOrder = new Orders();
        newOrder.setId( orderId );
        newOrder.setUserId( userId );

        UserAddress userAddress = addressService.queryUserAdress( userId, addressId );

        // 集成Feign之前的 负载调用
//        ServiceInstance instance = client.choose( "FOODIE-USER-SERVICE" );
//        String url = String.format( "http://%s:%s/address-api/queryAdress?userId=%s&addressId=%s",
//                instance.getHost(),
//                instance.getPort(),
//                userId, addressId);
//        UserAddress userAddress = restTemplate.getForObject( url, UserAddress.class );

        newOrder.setReceiverName( userAddress.getReceiver() );
        newOrder.setReceiverMobile( userAddress.getMobile() );
        newOrder.setReceiverAddress( userAddress.getProvince() +" "+ userAddress.getCity()+" "+ userAddress.getDistrict()+" "+ userAddress.getDetail() );

        newOrder.setPostAmount( postAmount );

        newOrder.setPayMethod( payMethod );
        newOrder.setLeftMsg( leftMsg );

        newOrder.setIsComment( YesOrNo.NO.type );
        newOrder.setIsDelete( YesOrNo.NO.type );

        newOrder.setCreatedTime( new Date(  ) );
        newOrder.setUpdatedTime( new Date(  ) );

        //2. 循环根据itemSpecIds
        String[] itemSpecIdArr = itemSpecIds.split( "," );
        Integer totalAmount = 0;     //商品原价累计
        Integer realPayAmount = 0;   //优惠后的实际支付价格累计

        // 待清空的商品
        List<ShopcartBO> toBeRemoveShopcartList = new ArrayList<>(  );
        for (String itemSpecId : itemSpecIdArr) {

            ShopcartBO shopcartBO = getBuyCountsFromShopcart( shopcartList, itemSpecId );
            toBeRemoveShopcartList.add( shopcartBO );

            // 整合Redis后，商品购买的数量重新从Redis的购物车中获取
            int buyCounts = Integer.parseInt( shopcartBO.getBuyCounts() );

            // 2.1 根据规格ID，查询规格的具体信息，主要获取价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById( itemSpecId );
//            ServiceInstance instanceItem = client.choose( "FOODIE-ITEM-SERVICE" );
//            String urlSpec = String.format( "http://%s:%s/item-api/singleItemSpec?specId=%s",
//                    instanceItem.getHost(),
//                    instanceItem.getPort(),
//                    itemSpecId );
//
//            ItemsSpec itemsSpec = restTemplate.getForObject( urlSpec, ItemsSpec.class );


            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            // 2.2 根据商品ID，获取商品信息以及商品图片
            String itemId = itemsSpec.getItemId();

            Items items = itemService.queryItemById( itemId );
//            String urlItem = String.format( "http://%s:%s/item-api/item?itemId=%s",
//                    instanceItem.getHost(),
//                    instanceItem.getPort(),
//                    itemId );
//            Items items = restTemplate.getForObject( urlItem, Items.class );

            String mainImgUrl = itemService.queryItemMainImgById( itemId );

//            String urlImg = String.format( "http://%s:%s/item-api/primaryImage?itemId=%s",
//                    instanceItem.getHost(),
//                    instanceItem.getPort(),
//                    itemId );
//            String mainImgUrl = restTemplate.getForObject( urlImg, String.class );

            // 2.3 循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems subOrder = new OrderItems();
            subOrder.setId( subOrderId );
            subOrder.setOrderId( orderId );
            subOrder.setItemId( itemId );
            subOrder.setItemName( items.getItemName() );
            subOrder.setItemImg( mainImgUrl );
            subOrder.setBuyCounts( buyCounts );
            subOrder.setItemSpecId( itemSpecId );
            subOrder.setItemSpecName( itemsSpec.getName() );
            subOrder.setPrice( itemsSpec.getPriceDiscount() );
            orderItemsMapper.insert( subOrder );

            // 2.4 在用户提交订单后台，规格表中要扣除库存
            itemService.decreaseItemSpecStock( itemSpecId, buyCounts );

//            String urldecrease = String.format( "http://%s:%s/item-api/decreaseStock?specId=%s&buyCounts=%s",
//                    instanceItem.getHost(),
//                    instanceItem.getPort(),
//                    itemSpecId, buyCounts );
//            restTemplate.postForLocation( urldecrease ,null);

        }

        newOrder.setTotalAmount( totalAmount );
        newOrder.setRealPayAmount( realPayAmount );
        ordersMapper.insert( newOrder );

        //3. 保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId( orderId );
        waitPayOrderStatus.setOrderStatus( OrderStatusEnum.WAIT_PAY.type );
        waitPayOrderStatus.setCreatedTime( new Date(  ) );
        orderStatusMapper.insert( waitPayOrderStatus );

        //4. 构建商户订单，用于传给支付中
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId( orderId );
        merchantOrdersVO.setMerchantUserId( userId );
        merchantOrdersVO.setAmount( realPayAmount + postAmount );
        merchantOrdersVO.setPayMethod( payMethod );

        //5. 构建自定义的订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId( orderId );
        orderVO.setMerchantOrdersVO( merchantOrdersVO );
        orderVO.setToBeRemoveShopcartList(toBeRemoveShopcartList);
        return orderVO;
    }

    private ShopcartBO getBuyCountsFromShopcart(List<ShopcartBO> shopcartBOList, String itemSpecId) {
        Optional<ShopcartBO> shopcartBO = shopcartBOList.stream().filter( sc -> sc.getSpecId().equals( itemSpecId ) ).findFirst();
        return shopcartBO.orElse( null );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId( orderId );
        paidStatus.setOrderStatus( orderStatus );
        paidStatus.setPayTime( new Date(  ) );
        orderStatusMapper.updateByPrimaryKeySelective( paidStatus );
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey( orderId );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {
        //查询所有超时订单，超时则关闭交易
        OrderStatus queryOrder = new OrderStatus();
        queryOrder.setOrderStatus( OrderStatusEnum.WAIT_PAY.type );
        List<OrderStatus> select = orderStatusMapper.select( queryOrder );
        select.forEach( os->{
            // 获得订单创建时间
            Date createdTime = os.getCreatedTime();
            int daysBetween = DateUtil.daysBetween( createdTime, new Date() );
            if (daysBetween >= 1) {
                //超过一天，关闭订单
                doCloseOrder(os.getOrderId());
            }
        } );

    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId) {
        OrderStatus close = new OrderStatus();
        close.setOrderId( orderId );
        close.setOrderStatus( OrderStatusEnum.CLOSE.type );
        close.setCloseTime( new Date(  ) );
        orderStatusMapper.updateByPrimaryKeySelective( close );
    }
}
