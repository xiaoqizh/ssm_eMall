package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

//cookie就是存数据的
@Controller
public class CartController {
    @Autowired
    private ItemService itemService;
    @Value("${COOKIE_EXPIRE}")
    private Integer COOKIE_EXPIRE;
    @Autowired
    private CartService cartService;
    //购物车信息是保存在cookie里面的 并且是以json的形式保存的
    //传递过来的num的值就是 1
    //商品肯定是一个一个加的
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, Integer num,
                          HttpServletRequest request, HttpServletResponse response) {
        TbUser user = (TbUser) request.getAttribute("user");
        //判断用户是否登陆
        if (user!=null) {
            cartService.LogInAddCart(user.getId(),itemId,num);
            return "cartSuccess";
        }
        //先获得本地的cookie  存储的是购物车中的商品
        List<TbItem> cookie = getCookieList(request);
        //查看当前商品是否存在cookie当中  Long是一个对象 设置一个标志位
        boolean isExist = false;
        for (TbItem tbItem : cookie) {
            if (tbItem.getId().longValue() == itemId.longValue()) {
                isExist = true;
                tbItem.setNum(tbItem.getNum() + num);//默认一开始tbItem.getNum()的值是0
                break;//可以直接break
            }
        }
        if (!isExist) {//不存在
            TbItem itemById = itemService.getItemById(itemId);//设置数量
            itemById.setNum(num);
            String images = itemById.getImage();
            if (StringUtils.isNotBlank(images)) {//只取第一个照片
                String[] image = images.split(",");
                itemById.setImage(image[0]);
            }
            //1!!把商品保存到list里面去
            cookie.add(itemById);
        }
        //分类处理完了之后 就是重新设置Cookie 设置过期时间
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cookie), COOKIE_EXPIRE, true);
        return "cartSuccess";
    }

    //转到显示购物车的页面
    @RequestMapping("/cart/cart")
    public String toCard(HttpServletRequest request ,HttpServletResponse response) {
        //得到本地的cookie中的商品
        List<TbItem> cookieList = getCookieList(request);
        //首先判断用户是否登陆
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            //把cookie中的item合并到redis中
            cartService.mergeCart(user.getId(), cookieList);
            //在把存在cookie中的购物车删除 删除cookie其实就是把cookie的值设为null
            CookieUtils.deleteCookie(request, response, "cart");
            //在从redis中得到商品列表
            cookieList = cartService.getItemsFromRedis(user.getId());
            request.setAttribute("cartList", cookieList);
            return "cart";
        }

        //获取本地cookie
        request.setAttribute("cartList", cookieList);
        return "cart";
    }

    //cookie的获得是request  设置 是response
    //获取本地cookie
    private List<TbItem> getCookieList(HttpServletRequest request) {
        //获取存在本地的Cookie cookie是存储的json数据
        String json = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isBlank(json)) {//如果本地还没有cookie 那么就返回空的list
            return new ArrayList<>();
        }
        List<TbItem> tbItems = JsonUtils.jsonToList(json, TbItem.class);
        return tbItems;
    }

    //更新购物车数量   每次点击一下都是通过ajax请求的 所以就需要responseBody
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateItemNum(@PathVariable Long itemId, @PathVariable Integer num
            , HttpServletRequest request, HttpServletResponse response) {
        //判断用户是否登陆
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.updateItemNum(user.getId(), itemId, num);
            return E3Result.ok();
        }
        //获取本地的cookie
        List<TbItem> cookieList = getCookieList(request);
        for (TbItem tbItem : cookieList) {
            if (tbItem.getId().longValue() == itemId.longValue()) {
                tbItem.setNum(num);
                break;//这里的break只是为了节省性能
            }
        }
        //将更新好的 重新写入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cookieList), COOKIE_EXPIRE, true);
        //返回只是为了刷新 总价钱
        return E3Result.ok(true);
    }

    //删除购物车中的商品 并且要进行重定向
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.deleteItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }
        List<TbItem> cookieList = getCookieList(request);
        for (TbItem tbItem : cookieList) {
            if (tbItem.getId().longValue() == itemId.longValue()) {
                cookieList.remove(tbItem);
                break;//不break就会报错
            }
        }
        //写入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cookieList), COOKIE_EXPIRE, true);
        //有/ 就是绝对路径 相对于当前项目
        return "redirect:/cart/cart.html";//必须要带上.html 别的地方省略是因为jsp文件上写了html后缀
    }
}
