package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl  implements CartService{

    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbItemMapper itemMapper;
    //登陆状态下的添加购物车
    @Value("$(CART_RESOURCE)")
    private  String CART_RESOURCE;
    @Override
    public E3Result LogInAddCart(long userId,long itemId, int num) {
        //先查看redis里面有没有相应的信息
        String hget = jedisClient.hget(CART_RESOURCE + ":" + userId, itemId + "");
        if (StringUtils.isNotBlank(hget)) {//存在redis里面的肯定是正确的
            TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
            tbItem.setNum(tbItem.getNum()+num);
            jedisClient.hset(CART_RESOURCE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
            return E3Result.ok();
        }
        //如果之前没有商品信息 那么在进行查询
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        tbItem.setNum(1);//设置初始数量
        String images = tbItem.getImage();
        tbItem.setImage(images.split(",")[0]);
        jedisClient.hset(CART_RESOURCE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result mergeCart(long userId, List<TbItem> tbItemList) {
        //合并cookie与redis总保存的购物车
        for (TbItem tbItem : tbItemList) {
            LogInAddCart(userId, tbItem.getId(), tbItem.getNum());
        }
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getItemsFromRedis(long userId) {
        //得到全部的商品json
        List<String> ItemJsons= jedisClient.hvals(CART_RESOURCE + ":" + userId);
        List<TbItem> redisItemsList = new ArrayList<>();
        for (String itemJson : ItemJsons) {
            TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);
            redisItemsList.add(tbItem);
        }
        return redisItemsList;
    }

    @Override
    public E3Result updateItemNum(long userId, long itemId, int num) {
        //itemId对应的是一个TBItem对象的json串  并不是只是num信息
        String json = jedisClient.hget(CART_RESOURCE + ":" + userId, itemId + "");
        TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
        item.setNum(num);
        jedisClient.hset(CART_RESOURCE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return E3Result.ok();
    }

    @Override
    public E3Result deleteItem(long userId, long itemId) {
        //删除商品
        jedisClient.hdel(CART_RESOURCE + ":" + userId, itemId+"");
        return E3Result.ok();
    }

    @Override
    public void deleteCartList(long userId) {
        jedisClient.del(CART_RESOURCE + ":" + userId);
    }
}
