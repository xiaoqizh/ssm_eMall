package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//一定养成爱写注释的好习惯
@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;
    //返回登陆页面
    @RequestMapping("/page/register")
    public String register() {
        return "register";
    }
    //分类别判断用户信息
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable String param, @PathVariable  Integer type) {
        E3Result e3Result = registerService.checkData(param, type);
        return e3Result;
    }
    //将注册信息保存到数据库
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser tbUser) {
        E3Result register = registerService.register(tbUser);
        return  register;
    }
}
