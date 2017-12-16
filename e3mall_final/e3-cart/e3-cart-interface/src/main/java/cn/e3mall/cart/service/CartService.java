package cn.e3mall.cart.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import java.util.List;
public interface CartService {
    E3Result LogInAddCart(long userId, long itemId, int num);
    E3Result mergeCart(long userId,List<TbItem> tbItemList);
    List<TbItem> getItemsFromRedis(long userId);
    E3Result updateItemNum(long userId, long itemId, int num);
    E3Result deleteItem(long userId, long itemId);
    void deleteCartList(long userId);
}
