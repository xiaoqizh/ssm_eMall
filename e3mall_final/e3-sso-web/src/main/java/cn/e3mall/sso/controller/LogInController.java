package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogInController {

    @Autowired
    private LoginService loginService;
    //返回登陆的页面
    //还可以接收跳转的页面redirect
    @RequestMapping("/page/login")
    public String userLogin(String redirect, Model model) {
        //让jsp页面进行跳转
        model.addAttribute("redirect", redirect);
        System.out.println(redirect);
        return "login";
    }
    //判断登陆是否成功的
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public E3Result userLogin(String username, String password ,
                 HttpServletRequest request, HttpServletResponse response) {
        //进行登陆判断
        E3Result result = loginService.userLogin(username, password);
        if (result.getStatus() == 200) {
            //那么就设置cookie
            CookieUtils.setCookie(request, response, "token",result.getData().toString());
        }
        return result;
    }
}
