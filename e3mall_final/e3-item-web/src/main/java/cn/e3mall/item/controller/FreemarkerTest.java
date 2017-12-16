package cn.e3mall.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FreemarkerTest {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genhtml")
    @ResponseBody
    public String testFreemarker() throws Exception {
        //这个配置是配置的ftl文件所在的文件夹
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //得到模板
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap();
        data.put("hello","woshiniabba");
        //加载模板 进行填充
        Writer writer = new FileWriter(new File("D:/hello2.txt"));
        template.process(data, writer);
        writer.close();
        return "ok";
    }
}
