package cn.e3mall.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
/**
 *
 * <p>Title: PictureController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
//用于保存图片的
@Controller
public class PictureController {
	//value注解
  @Value("${IMAGE_SERVER_URL}")
  private String IMAGE_SERVER_URL;
  private File file;
  private FileWriter writer;
  private String separator = System.getProperty("line.separator");
  //prodeces 指定返回的形式 还可以设定编码集
  @RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
  @ResponseBody
  public String fileupload(MultipartFile uploadFile){
	  try {
		String originalFilename = uploadFile.getOriginalFilename();
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
		FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
		String uploadFile2 = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
		System.out.println(uploadFile2);
		String url = IMAGE_SERVER_URL+uploadFile2;
		  file= new File("D:\\myPic.txt");
		  writer = new FileWriter(file,true);
		  try {
			writer.write(url+separator);
		  }catch (Exception e){
			  System.out.println("存储失败");
		  }
		  finally {
			  writer.close();
		  }
		  System.out.println(url);
		  Map result =new HashMap<>();
		  result.put("error", 0);
		  result.put("url",url);
	    return JsonUtils.objectToJson(result);
	} catch (Exception e) {
		e.printStackTrace();
		Map result =new HashMap<>();
		result.put("error", 1);
		result.put("message","filed");
	    return JsonUtils.objectToJson(result);
	}  
  }
}
