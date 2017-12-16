package cn.e3mall.search.dao;


import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//dao层的注解
@Repository
public class SearchDao {
    @Autowired
    private SolrServer solrServer;
    //这里是把query封装起来  效率更高！
    public SearchResult search(SolrQuery query) throws SolrServerException {
        QueryResponse queryResponse = solrServer.query(query);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        SearchResult searchResult = new SearchResult();
        long numFound = solrDocumentList.getNumFound();
        //取高亮模式  开启高亮模式是在query里面完成的
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        //设置总的查询数量
        List<SearchItem> list = new ArrayList<>();
        for (SolrDocument entries : solrDocumentList) {
            SearchItem searchItem = new SearchItem();
            searchItem.setId((String) entries.get("id"));
            searchItem.setCategory_name((String) entries.get("item_category_name"));
            searchItem.setImage((String) entries.get("item_image"));
            searchItem.setPrice((Long) entries.get("item_price"));
            searchItem.setSell_point((String) entries.get("item_sell_point"));
            List<String> stringList = highlighting.get(entries.get("id")).get("item_title");
            String title = "";
            if (stringList != null && stringList.size() > 0) {
                title = stringList.get(0);
            } else {
                title=(String) entries.get("item_title");
            }
            searchItem.setTitle(title);
            list.add(searchItem);
        }
        searchResult.setRecordCount(numFound);
        searchResult.setItemList(list);
        return  searchResult;
    }
}
