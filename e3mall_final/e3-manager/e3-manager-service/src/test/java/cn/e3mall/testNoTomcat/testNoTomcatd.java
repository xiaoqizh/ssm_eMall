package cn.e3mall.testNoTomcat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class testNoTomcatd {
 @Test
 public void test() throws IOException{
	 ApplicationContext applicationContext =new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
	 System.in.read();
	 //只是为了测试tomcat在发布服务中是起不到什么作用的  
 }
}
