package cn.e3mall.order.interceptor;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//在提交订单时进行拦截  判断用户是否登陆
public class OrderInterceptor  implements HandlerInterceptor{
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CartService cartService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object o) throws Exception {
        //得到本地的cookie
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        if (StringUtils.isBlank(token)) {//如果未登录 那就转发
            httpServletResponse.sendRedirect("http://localhost:8089/page/login?redirect="
                    +httpServletRequest.getRequestURL());
            return  false;//拦截不让通过
        }
        //如果存在那么就判断是否已经过期
        E3Result e3Result = tokenService.checkToken(token);
        //如果已经过期
        if (e3Result.getStatus() != 200) {//还是让他重新登录
            httpServletResponse.sendRedirect("http://localhost:8089/page/login?redirect="
                    +httpServletRequest.getRequestURL());
            return  false;//拦截不让通过
        }
        //用户名已经存在了 合并cookie中的和redis中的购物车列表
        TbUser user = (TbUser) e3Result.getData();
        //原来的一登陆就合并cookie的是用户一开始未登录 后来登陆又接着购物 此时购物车信息全在redis中
        //现在的合并是用户一直没有登陆  只有在结算的时候才进行登陆   此时购物车信息仍在redis中 所以需要
        //得到本地cookie中的信息
        String cartJson = CookieUtils.getCookieValue(httpServletRequest, "cart", true);
        if (StringUtils.isNotBlank(cartJson)) {
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cartJson, TbItem.class));
        }
        httpServletRequest.setAttribute("user",user);
        return true; //放行
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }
}
