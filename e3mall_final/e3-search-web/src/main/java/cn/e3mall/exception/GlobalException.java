package cn.e3mall.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalException implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse, Object o, Exception e) {
        //打印log日志  用log4j 我就不写了
//        返回一个错误页面
        ModelAndView modelAndView = new ModelAndView();
        //注意跟试图解析器的 配置
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
