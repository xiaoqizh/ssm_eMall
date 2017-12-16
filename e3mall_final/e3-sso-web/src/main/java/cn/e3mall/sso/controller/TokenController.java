package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TokenController {
    //处理session的表现层
    @Autowired
    private TokenService tokenService;
    //这里不需要加上SESSION:因为前面已经设置了
    //处理jsonp请求 jsonp就是处理跨域json  默认json如果跨域会被拦截的
    //jsonp请求多了一个callback参数
    // 如果客户端传值callback，那么就会以jsonp的形式返回给客户端
    //如果客户端没有传值callback,那么默认以json的形式返回给客户端
    //MappingJacksonValue是spring4.1 之后的版本  这个项目的版本是4.2
    //callback是一个方法名
       @RequestMapping("/user/token/{token}")
       @ResponseBody
     //一个参数对应一个具体的参数值
      public Object UserToken(@PathVariable  String token,String callback) {
        E3Result e3Result = tokenService.checkToken(token);
        //判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            //把返回的信息进行包装
            MappingJacksonValue jacksonValue = new MappingJacksonValue(e3Result);
            //设置jsonp的函数名称
            jacksonValue.setJsonpFunction(callback);
            return  jacksonValue;
        }
        return  e3Result;
    }

    //如果是String形式的返回  那么浏览器会认为是一个html信息
      //这个是比较古老的版本
   /* @RequestMapping(value = "/user/token/{token}",
                    produces = "application/json;charset=utf-8")
    @ResponseBody
    public String  UserToken(@PathVariable  String token,String callback) {
        E3Result e3Result = tokenService.checkToken(token);
        //判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            //固定形式的包装 以后可以不用了 比较麻烦
            String message = callback + "(" + JsonUtils.objectToJson(e3Result)+");";
            return  message;
        }
        return  JsonUtils.objectToJson(e3Result);
    }*/
}
