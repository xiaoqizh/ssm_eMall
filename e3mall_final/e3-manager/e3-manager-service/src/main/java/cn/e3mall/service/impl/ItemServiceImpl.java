package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper  itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	//新的注解  因为里面有两个destination  resource是根据id 来进行注入的
	@Resource
	private Destination topicDestination;
	//添加缓存
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO_PRE}")
	private  String ITEM_INFO_PRE;
	@Value("${EXPIRE_TIME}")
	private  Integer EXPIRE_TIME;
	//添加缓存先查 然后在添加
	@Override
	public TbItem getItemById(long itemId) {
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		try {
			String json = jedisClient.get(ITEM_INFO_PRE+":"+itemId+":BASE");
			if (StringUtils.isNotBlank(json)) {
				return JsonUtils.jsonToPojo(json, TbItem.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			try {
				//设置缓存  缓存不能影响正确的业务逻辑
				jedisClient.set(ITEM_INFO_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(list.get(0)));
				//设置过期时间
				jedisClient.expire(ITEM_INFO_PRE+":"+itemId+":BASE", EXPIRE_TIME);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		PageHelper pageHelper = new PageHelper();
		pageHelper.startPage(page, rows);
		TbItemExample example  = new TbItemExample();
		//ItemMapper是注入进来的
		List list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		long total = pageInfo.getTotal();
		result.setTotal(total);
		result.setRows(list);
		return result;
	}

	@Override
	public E3Result addItem(TbItem tbItem, String desc) {
		//得到传递过来的消息 封装到java类当中
		final long genItemId = IDUtils.genItemId();
		tbItem.setId(genItemId);
		//商品状态，1-正常，2-下架，3-删除
		tbItem.setStatus((byte) 1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		//插入数据
		itemMapper.insert(tbItem);
		//向tb_item_desc存数据
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(genItemId);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		tbItemDesc.setItemDesc(desc);
		//这是插入数据
		itemDescMapper.insert(tbItemDesc);
		//在提交之前发送消息
		//传递的是id
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(genItemId + "");
				return message;
			}
		});
		return E3Result.ok();
	}

	@Override
	public TbItemDesc getDescById(long itemId) {
		try {
			String json = jedisClient.get(ITEM_INFO_PRE+":"+itemId+":DESC");
			if (StringUtils.isNotBlank(json)) {
				return JsonUtils.jsonToPojo(json, TbItemDesc.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		//获取商品描述
		try {
			//设置缓存  缓存不能影响正确的业务逻辑
			jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
			//设置过期时间
			jedisClient.expire(ITEM_INFO_PRE+":"+itemId+":DESC", EXPIRE_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

}
