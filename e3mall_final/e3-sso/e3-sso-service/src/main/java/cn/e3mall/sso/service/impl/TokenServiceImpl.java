package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private  Integer SESSION_EXPIRE;
    //服务层获取token
    @Override
    public E3Result checkToken(String token) {
        //得到redis中存储的用户信息
        String  json= jedisClient.get("SESSION:" + token);
        if (StringUtils.isBlank(json)) {
            return E3Result.build(201, "登陆已过期,请重新登录");
        }
        //否则就返回信息 并且重新设置过期时间 处于活跃状态
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
        return E3Result.ok(tbUser);
    }
}
