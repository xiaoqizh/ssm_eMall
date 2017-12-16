package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {
    //首先返回index.jsp
  @RequestMapping("/")
  public String showIndex(){
	  return "index";
  }
  


  @RequestMapping("/{page}")
  public String showPage(@PathVariable String page)
  {
	  return page;
  }
}
