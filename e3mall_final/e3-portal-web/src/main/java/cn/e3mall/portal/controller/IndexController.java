package cn.e3mall.portal.controller;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {
	/**
	 * 这里是使用默认 web.xml中请求index.html 来进行拦截的S
	 * <p>Title: showIndex</p>
	 * <p>Description: </p>
	 * @return
	 */
	@Value("${CATEGORY_INDEX_ID}")
	private  long category_id;
	@Autowired
	private ContentService contentService;
    @RequestMapping("/index.html")
    public String showIndex(Model model){
	  List<TbContent> contentList = contentService.getContentList(category_id);
	  //向域中添加数据
	  model.addAttribute("ad1List", contentList);
	  return "index";
  }
}
