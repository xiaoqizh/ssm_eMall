import com.sun.scenario.animation.shared.TimerReceiver;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.util.List;
import java.util.Map;
/*

public class testQuery {
   */
/* @Test
    public void test1() throws SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
        SolrQuery query = new SolrQuery();
        query.set("q","*:*");
        query.set("df", "item_title");
        QueryResponse queryResponse = solrServer.query(query);
        SolrDocumentList results = queryResponse.getResults();
        System.out.println(results.getNumFound());
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("item_title"));
        }*//*

    }

    //带高亮显示
    @Test
    public void test2() throws SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q","手机");
        solrQuery.set("df", "item_title");
        //设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em>");
        solrQuery.setHighlightSimplePost("</em>");
        QueryResponse query = solrServer.query(solrQuery);
        SolrDocumentList results = query.getResults();
        //高亮数据和原来的数据是分开显示的
        //格式可以对比solr服务器来看
        Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
        for (SolrDocument result : results) {
            String title=null;
            //list的返回值肯定只有一个！
            List<String> list = highlighting.get(result.get("id")).get("item_title");
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) result.get("item_title");
            }
            System.out.println(title);
            System.out.println(result.get("item_title"));
        }
    }
}
*/
