package cn.e3mall.search.controller;
import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Value("${ROWS}")
    private  Integer rows;
    @RequestMapping("/search")
    public String search(String keyword,
                         @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {
        //web.xml中解决的只是post的乱码请求
        keyword = new String(keyword.getBytes("iso-8859-1"), "utf-8");
        //得到查询结果
        //page一开始并不会传递 只是在下一页的时候才会进行传递
        SearchResult searchResult = searchService.search(keyword, page, rows);
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("recourdCount", searchResult.getRecordCount());
        model.addAttribute("itemList", searchResult.getItemList());
        return "search";
    }
}
