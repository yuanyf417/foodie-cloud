package com.imooc.order.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.order.mapper.OrderStatusMapper;
import com.imooc.order.mapper.OrdersMapper;
import com.imooc.order.mapper.OrdersMapperCustom;
import com.imooc.order.pojo.OrderStatus;
import com.imooc.order.pojo.Orders;
import com.imooc.order.pojo.vo.MyOrdersVO;
import com.imooc.order.pojo.vo.OrderStatusCountsVO;
import com.imooc.order.service.center.MyOrdersService;
import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.pojo.PagedGridResult;
import com.imooc.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  foodie-dev
 *  我的订单实现类
 * @author: YYF
 * @create: 2020-05-09 13:45
 **/
@RestController
public class MyOrdersServiceImpl extends BaseService implements MyOrdersService {

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>(  );
        map.put( "userId", userId );
        if (orderStatus != null) {
            map.put( "orderStatus" ,orderStatus );
        }

        PageHelper.startPage( page, pageSize );
        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders( map );

        return setterPagedGrid(list,page);
    }

    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId( userId );
        orders.setId( orderId );
        orders.setIsDelete( YesOrNo.NO.type );

        return ordersMapper.selectOne( orders );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus updateOrderStatus = new OrderStatus();
        updateOrderStatus.setDeliverTime( new Date(  ) );
        updateOrderStatus.setOrderStatus( OrderStatusEnum.WAIT_RECEIVE.type );

        Example example = new Example( OrderStatus.class );
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo( "orderId", orderId );
        criteria.andEqualTo( "orderStatus", OrderStatusEnum.WAIT_DELIVER .type);

        orderStatusMapper.updateByExampleSelective( updateOrderStatus, example );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus( OrderStatusEnum.SUCCESS.type );
        updateOrder.setSuccessTime( new Date(  ) );

        Example example = new Example( OrderStatus.class );
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo( "orderId", orderId );
        criteria.andEqualTo( "orderStatus", OrderStatusEnum.WAIT_RECEIVE.type );

        int i = orderStatusMapper.updateByExampleSelective( updateOrder, example );


        return i == 1;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {

        Orders updateOrder = new Orders();
        updateOrder.setIsDelete( YesOrNo.YES.type );
        updateOrder.setUpdatedTime( new Date(  ) );

        Example example = new Example( Orders.class );
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo( "id", orderId );
        criteria.andEqualTo( "userId", userId );

        int i = ordersMapper.updateByExampleSelective( updateOrder, example );

        return i == 1;
    }

    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        map.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPayCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", YesOrNo.NO.type);
        int waitCommentCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        OrderStatusCountsVO countsVO = new OrderStatusCountsVO(waitPayCounts,
                waitDeliverCounts,
                waitReceiveCounts,
                waitCommentCounts);
        return countsVO;
    }

    @Override
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put( "userId", userId );

        PageHelper.startPage( page, pageSize );
        List<OrderStatus> list = ordersMapperCustom.getMyOrderTrend( map );

        return setterPagedGrid( list, page );
    }

    @Override
    public IMOOCJSONResult checkoutUserOrder(String orderId, String userId) {
        Orders orders = queryMyOrder( userId, orderId );

        if (orders == null) {
            return IMOOCJSONResult.errorMap( "订单不存在！" );
        }
        return IMOOCJSONResult.ok( orders );
    }

}
