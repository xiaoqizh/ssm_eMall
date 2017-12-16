package cn.e3mall.search.message;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
public class ItemAddListener implements MessageListener {
   @Autowired
   private ItemMapper itemMapper;
   @Autowired
   private SolrServer solrServer;
    @Override
    public void onMessage(Message message) {
        //传递过来的是商品的id
        //然后通过id查询信息 插入到索引库当中
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = new Long(text);
            Thread.sleep(1000);
            SearchItem searchItem = itemMapper.getItemById(itemId);
            //然后在加入到索引库当中
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id",searchItem.getId());
            document.setField("item_title",searchItem.getTitle());
            document.setField("item_sell_point",searchItem.getSell_point());
            document.setField("item_price",searchItem.getPrice());
            document.setField("item_image",searchItem.getImage());
            document.setField("item_category_name",searchItem.getCategory_name());
            solrServer.add(document);
            solrServer.commit();
        }
        catch (Exception e) {
            e.printStackTrace(); }}
}
