package cn.e3mall.item.message;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class GenHtmlListener implements MessageListener{

    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${GEN_HTML_ADDRESS}")
    private  String GEN_HTML_ADDRESS;
    @Override
    public void onMessage(Message message) {
        //传递过来的还是商品id
        try {
            TextMessage textMessage = (TextMessage) message;
            String myitemId = textMessage.getText();
            Long itemId = new Long(myitemId);
            Thread.sleep(1000);
            TbItem tbItem = itemService.getItemById(itemId);
            //封装一个对象
            Item item = new Item(tbItem);
            TbItemDesc desc = itemService.getDescById(itemId);
            Map data = new HashMap();
            data.put("item",item);
            data.put("itemDesc", desc);
            //freemarker配置
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            Writer writer = new FileWriter(new File(GEN_HTML_ADDRESS+itemId+".html"));
            template.process(data,writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
