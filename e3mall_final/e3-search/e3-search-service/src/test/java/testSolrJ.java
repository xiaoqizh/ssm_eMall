import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class testSolrJ {
    @Test
    public void test1() throws IOException, SolrServerException {
     //solr都是对应的一个个的文档
        //创建服务器  默认就是collection1
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id","doc1");
        document.setField("item_title","my1");
        document.setField("item_price",1000);
        solrServer.add(document);
        //最后一定要提交
        solrServer.commit();
        //删除就是
        //solrServer.deleteByQuery("id:doc1");
    }

    @Test
    public void test2() throws IOException, SolrServerException {
      /*  SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
        solrServer.deleteByQuery("*:*");
        solrServer.commit();*/
    }

}
