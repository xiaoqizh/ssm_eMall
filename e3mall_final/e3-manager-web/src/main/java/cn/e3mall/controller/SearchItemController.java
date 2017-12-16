package cn.e3mall.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {


    //用于从数据库导入到solr的
    @Autowired
    private SearchItemService searchItemService;
    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importItemList() {
       return    searchItemService.importAllItems();
    }

}
