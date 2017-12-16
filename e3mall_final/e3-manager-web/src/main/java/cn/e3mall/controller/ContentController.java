package cn.e3mall.controller;

import java.util.List;

import cn.e3mall.common.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;

@Controller
public class ContentController {

   @Autowired
   private ContentCategoryService categoryService;

	//查询所有的产品分类
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList (@RequestParam(name="id",defaultValue="0")long parentId)
	{
		 List<EasyUITreeNode> categoryList = categoryService.getContentCategoryList(parentId);
		 return categoryList;
	}

   //创建一个产品分类
	@RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
	@ResponseBody
	public E3Result addContentCategory(long  parentId,String name)
	{
		E3Result result = categoryService.addContentCategory(parentId, name);
		return  result;
	}
	
}
