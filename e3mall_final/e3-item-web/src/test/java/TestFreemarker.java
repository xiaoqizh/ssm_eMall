import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class TestFreemarker {
    //freeMarker 是网页静态化   其实就是填空
    //也就是把变量替换成常量  ftl  是freemarker的默认后缀名  不用记住这些步骤
    @Test
    public void genFile() throws Exception {
        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 第二步：设置模板文件所在的路径。
        configuration.setDirectoryForTemplateLoading(new File("D:\\intellij_Idea\\e3mall_final\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
        // 第三步：设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding("utf-8");
        // 第四步：加载一个模板，创建一个模板对象。
//        Template template = configuration.getTemplate("hello.ftl");
        Template template = configuration.getTemplate("student.ftl");
        // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
        Map dataModel = new HashMap<>();
        //向数据集中添加数据
        dataModel.put("hello", "this is my first freemarker test.");
        Student student = new Student("18","zhangsan","hengshui");
        dataModel.put("student", student);
        List<Student> students = new ArrayList<>();
        students.add(new Student("18","zhangsan","hengshui"));
        students.add(new Student("18","zhangsan","hengshui"));
        students.add(new Student("18","zhangsan","hengshui"));
        students.add(new Student("18","zhangsan","hengshui"));
        students.add(new Student("18","zhangsan","hengshui"));
        dataModel.put("students", students);
        dataModel.put("date",new Date());
        // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
//        Writer out = new FileWriter(new File("D:/hello.txt"));
        Writer out = new FileWriter(new File("D:/student2.html"));
        // 第七步：调用模板对象的process方法输出文件。
        template.process(dataModel, out);
        // 第八步：关闭流。
        out.close();
    }

}
