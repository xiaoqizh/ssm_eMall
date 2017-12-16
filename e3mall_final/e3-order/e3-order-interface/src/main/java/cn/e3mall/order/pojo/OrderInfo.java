package cn.e3mall.order.pojo;
import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

import java.util.List;

//这个类就是包装从crder-caetjsp页面传递过来信息
//tbItemList是订单中的商品信息
//shipping是订单的物流信息
public class OrderInfo extends TbOrder {
    //订单的列表项 TbOrderItem
    private   List<TbOrderItem> orderItems ;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
