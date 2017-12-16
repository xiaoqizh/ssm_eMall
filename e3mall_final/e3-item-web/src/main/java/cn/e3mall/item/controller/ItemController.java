package cn.e3mall.item.controller;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @RequestMapping("/item/{itemId}")
    public String itemInfo(@PathVariable Long itemId, Model model) {
        TbItem tbItem = itemService.getItemById(itemId);
        //传递的不是tbItem
        TbItemDesc desc = itemService.getDescById(itemId);
        Item item = new Item(tbItem);
        System.out.println(item.getImages());
        model.addAttribute("item", item);
        //商品描述和商品是区分开的
        model.addAttribute("itemDesc", desc);
        return "item";
    }
}
