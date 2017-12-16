package cn.e3mall.order.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Value("${ORDER_KEY}")
    private  String ORDER_KEY;
    @Value("${ORDER_id}")
    private  String ORDER_id;
    @Autowired
    private JedisClient jedisClient;
    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        //把相应的数据存入数据库
        if (!jedisClient.exists(ORDER_KEY)) {
            jedisClient.set(ORDER_KEY, ORDER_id);
        }
        orderInfo.setStatus(1);
        orderInfo.setUpdateTime(new Date());
        orderInfo.setCreateTime(new Date());
        //获取存储在redis中的数据
         String  orderId = jedisClient.incr(ORDER_KEY).toString();
        orderInfo.setOrderId(orderId);
        orderMapper.insert(orderInfo);//mybatis只会存储在数据库中有相应数据的内容
        //存储商品列表
        List<TbOrderItem> tbOrderItemList = orderInfo.getOrderItems();
        for (TbOrderItem tbOrderItem : tbOrderItemList) {
            String itemId = jedisClient.incr("tbItemId").toString();//自己的id 可以随便设置
            tbOrderItem.setId(itemId);
            tbOrderItem.setOrderId(orderId);//专属于一个订单号的
            //向数据库中插入数据
            orderItemMapper.insert(tbOrderItem);
        }
        //存储物流信息  要根据数据库中的表一起来
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setUpdated(new Date());
        orderShipping.setCreated(new Date());
        orderShippingMapper.insert(orderShipping);
        //一般订单号是要返回给客户看的
        return E3Result.ok(orderId);
    }
}
