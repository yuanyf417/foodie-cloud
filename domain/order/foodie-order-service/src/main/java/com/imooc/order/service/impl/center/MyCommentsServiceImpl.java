package com.imooc.order.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.item.pojo.ItemsComments;
import com.imooc.order.mapper.OrderItemsMapper;
import com.imooc.order.mapper.OrderStatusMapper;
import com.imooc.order.mapper.OrdersMapper;
import com.imooc.order.mapper.OrdersMapperCustom;
import com.imooc.order.pojo.OrderItems;
import com.imooc.order.pojo.OrderStatus;
import com.imooc.order.pojo.Orders;
import com.imooc.order.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.order.pojo.vo.OrderStatusCountsVO;
import com.imooc.order.service.center.MyCommentsService;
import com.imooc.pojo.PagedGridResult;
import com.imooc.service.BaseService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: foodie-dev
 * @description: 评论实现类
 * @author: YYF
 * @create: 2020-05-09 16:00
 **/
@RestController
public class MyCommentsServiceImpl extends BaseService implements MyCommentsService {

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

//    @Autowired
//    private OrdersMapperCustom ordersMapperCustom;

    @Autowired
    private Sid sid;

    //    @Autowired
//    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    private LoadBalancerClient client;
    @Autowired
    private RestTemplate restTemplate;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {

        OrderItems queryOrderItem = new OrderItems();
        queryOrderItem.setOrderId( orderId );
        return orderItemsMapper.select( queryOrderItem );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(String orderId, String userId, List<OrderItemsCommentBO> commentBOList) {


        ItemsComments updateOrderItems = new ItemsComments();

        // 1. 保存评论
        commentBOList.stream().forEach( oic->{
            String commentId = sid.nextShort();
            oic.setCommentId( commentId );
        } );

        Map<String,Object> map = new HashMap<>(  );
        map.put( "userId", userId );
        map.put( "commentList", commentBOList );

        //itemsCommentsMapperCustom.saveComments( map );
        // 将上述调用 修改为远程方法调用
        ServiceInstance instance = client.choose( "FOODIE-ITEM-SERVICE" );
        String url = String.format( "http://%s:%s/item-comments-api/saveComments",
                instance.getHost(),
                instance.getPort() );
        restTemplate.postForLocation( url, map );

        // 2. 修改订单评论状态 -> 已评论

        Orders updateOrders = new Orders();
        updateOrders.setId( orderId );
        updateOrders.setIsComment( YesOrNo.YES.type );

        ordersMapper.updateByPrimaryKeySelective( updateOrders );

        // 3. 更新订单表的评论时间

        OrderStatus updateOrderStatus = new OrderStatus();
        updateOrderStatus.setOrderId( orderId );
        updateOrderStatus.setCommentTime( new Date(  ) );

        orderStatusMapper.updateByPrimaryKeySelective( updateOrderStatus );

    }

    // TODO 移到itemcommentservice
    /*@Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {

        Map<String,Object> map = new HashMap<>();
        map.put( "userId", userId );

        PageHelper.startPage( page, pageSize );
        List<MyCommentVO> voList = itemsCommentsMapperCustom.queryMyComments( map );

        return setterPagedGrid(voList, page);
    }*/

    /*@Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {

        Map<String,Object> map = new HashMap<>(  );
        map.put( "userId", userId );

        map.put( "orderStatus", OrderStatusEnum.WAIT_PAY.type );
        int waitPayCounts = ordersMapperCustom.getMyOrderStatusCounts( map );

        map.put( "orderStatus", OrderStatusEnum.WAIT_DELIVER.type );
        int waitDeliverCounts = ordersMapperCustom.getMyOrderStatusCounts( map );

        map.put( "orderStatus", OrderStatusEnum.WAIT_RECEIVE.type );
        int waitReceiveCounts = ordersMapperCustom.getMyOrderStatusCounts( map );

        map.put( "orderStatus", OrderStatusEnum.SUCCESS.type );
        map.put( "isComment", YesOrNo.NO.type );
        int waitCommentCounts = ordersMapperCustom.getMyOrderStatusCounts( map );

        return new OrderStatusCountsVO( waitPayCounts, waitDeliverCounts, waitReceiveCounts, waitCommentCounts );
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getMyOrderTrend(String userId, Integer page, Integer pageSize) {

        Map<String,Object> map = new HashMap<>(  );
        map.put( "userId", userId );

        PageHelper.startPage( page , pageSize);
        List<OrderStatus> trend = ordersMapperCustom.getMyOrderTrend( map );

        return setterPagedGrid( trend, page );
    }*/
}
