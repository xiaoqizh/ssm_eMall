package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {


    /*这个才是真正查询数据库的！
     */
    /**
     *! 数据层中只是基本的查询 不设置具体的参数
     * 分页是在service层中进行设置的
     * 在控制层 调整分页过程中的参数
     * !!!
     * 无论是干什么 都是要有分页的！
     */
   @Autowired
   private SearchDao searchDao;
   //page是页面传过来的  rows是在controller层自己设置的！
    @Override
    public SearchResult search(String keyword, int page, int rows) throws Exception {
        SolrQuery query = new SolrQuery();
        query.setQuery(keyword);
        query.set("df", "item_title");
        //还有默认搜索 转义双引号
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style= \"color:red\">");
        query.setHighlightSimplePost("</em>");
        //还有设置分页！
        //其实就是从第0页开始的  只是为了之后的 page-1
        if (page <= 0) { page =1; }
        query.setStart(  (page-1)*rows  );
        query.setRows(rows);
        SearchResult searchResult = searchDao.search(query);
        int totalPages = (int) (searchResult.getRecordCount()/rows);
        if (totalPages % rows > 0) { totalPages++; }
        searchResult.setTotalPages(totalPages);
        return searchResult;
    }
}
