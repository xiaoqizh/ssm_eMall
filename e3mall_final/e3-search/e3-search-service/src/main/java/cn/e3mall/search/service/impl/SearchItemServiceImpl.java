package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//service 一定不要忘了注解！
@Service
public class SearchItemServiceImpl implements SearchItemService {

    /**
     * 准确的来说 这个是想solr中添加数据的
     * 千万不要忘了模块之间 的依赖
     * @return
     */
    @Autowired
    private ItemMapper itemMapper;
    //配置成httpSolrServer
    @Autowired
    private SolrServer solrServer;
    @Override
    public E3Result importAllItems() {
        List<SearchItem> itemList = itemMapper.getItemList();
        try {
            for (SearchItem searchItem : itemList) {
                SolrInputDocument document = new SolrInputDocument();
                document.setField("id",searchItem.getId());
                document.setField("item_title",searchItem.getTitle());
                document.setField("item_sell_point",searchItem.getSell_point());
                document.setField("item_price",searchItem.getPrice());
                document.setField("item_image",searchItem.getImage());
                document.setField("item_category_name",searchItem.getCategory_name());
                solrServer.add(document);
            }
            //不要忘了提交
            solrServer.commit();
            return  E3Result.ok();
        }
        catch (Exception e){
            e.printStackTrace();
            return E3Result.build(500, "创建失败");
        }
    }
}
