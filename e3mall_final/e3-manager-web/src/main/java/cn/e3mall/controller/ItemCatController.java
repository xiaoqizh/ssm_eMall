package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.service.ItemCatService;

@Controller
public class ItemCatController {


  @Autowired
  private ItemCatService itemCatService;
  //在新增分类时 选择一个分类
  @RequestMapping("/item/cat/list")
  @ResponseBody
  public List<EasyUITreeNode> getItemCatList(@RequestParam(name="id",defaultValue="0")long parentId){
	    List<EasyUITreeNode> catList = itemCatService.getCatList(parentId);
	    return catList;
  }
}
