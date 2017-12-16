package cn.e3mall.content.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

//一定不要忘了service
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;
    //JedisClient一定不要忘了要有注入
    @Autowired
    private JedisClient client ;

    @Override
    public E3Result addContent(TbContent tbContent) {
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        contentMapper.insert(tbContent);
        //进行缓存同步
        //缓存同步就是在进行增删改之后 把原来的删掉 不是删除大键  只是删除hash中的小键
        client.hdel(CONTENT_KEY, tbContent.getCategoryId().toString());
        return E3Result.ok();
    }
    //在首页下面进行缓存
    //根据cid进行查询
    @Override
    public List<TbContent> getContentList(long cid) {
        try {
            //添加的缓存不能影响原来的业务逻辑
            //先在缓存中查询有没有
            //redis中存储的都是字符串 不能有别的类型
            String hget = client.hget(CONTENT_KEY, cid + "");
            if (StringUtils.isNotBlank(hget)) {
                return JsonUtils.jsonToList(hget, TbContent.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> tbContents = contentMapper.selectByExampleWithBLOBs(example);
        try {
            //向缓存中添加数据
            //数据都是存储在json中的
            client.hset(CONTENT_KEY, cid + "", JsonUtils.objectToJson(tbContents));
        }catch (Exception e){
            e.printStackTrace();
        }
        return tbContents;
    }
}
