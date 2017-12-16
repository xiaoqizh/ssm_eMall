package cn.e3mall.cart.interceptor;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogInInteceptor implements HandlerInterceptor{
    @Autowired
    private TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //执行handler之前
        //判断用户是否登录
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        if (StringUtils.isBlank(token)) {//如果没登陆 直接返回
            return  true;
        }
        E3Result e3Result = tokenService.checkToken(token);
        if (e3Result.getStatus() != 200) {
            return  true;
        }
        TbUser  user = (TbUser) e3Result.getData();
        httpServletRequest.setAttribute("user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //执行handler之后
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //返回modelandView之前
    }
}
