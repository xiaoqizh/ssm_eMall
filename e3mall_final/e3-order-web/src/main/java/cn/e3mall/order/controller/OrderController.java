package cn.e3mall.order.controller;


import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Controller
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @RequestMapping("/order/order-cart")
    public String toOrderPage(HttpServletRequest request) {
        TbUser user = (TbUser) request.getAttribute("user");
        //得到用户存在redis中的购物车信息
        //这一步已经在拦截器中确保可以实现了
        List<TbItem> itemsFromRedis = cartService.getItemsFromRedis(user.getId());
        request.setAttribute("cartList",itemsFromRedis);
        return "order-cart";
    }
    @RequestMapping(value = "/order/create" ,method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
        //还可以在表现层在进行封装一下
        TbUser user = (TbUser) request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        //要分清楚实参和形参的区别
        E3Result result = orderService.createOrder(orderInfo);
        //这时候可以删除存储在redis中的购物车的信息
        cartService.deleteCartList(user.getId());
        request.setAttribute("orderId",result.getData());
        request.setAttribute("payment",orderInfo.getPayment());
        return "success";
    }
}
