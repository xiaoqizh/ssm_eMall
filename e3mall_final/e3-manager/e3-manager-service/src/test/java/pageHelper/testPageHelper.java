package pageHelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class testPageHelper {
  @Test
  public void  testPagehelper(){
   ApplicationContext context = new	 ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
	 TbItemMapper tbItemMapper = context.getBean(TbItemMapper.class);  
	 //从1开始的
	 PageHelper.startPage(2, 10);
	 TbItemExample example = new TbItemExample();
	 List<TbItem> selectByExample = tbItemMapper.selectByExample(example);
	 PageInfo<TbItem> pInfo  = new PageInfo<>(selectByExample);
	
	 System.out.println(pInfo.getList());
	 System.out.println(pInfo.getPages());
	 System.out.println(pInfo.getSize());
  }
}
