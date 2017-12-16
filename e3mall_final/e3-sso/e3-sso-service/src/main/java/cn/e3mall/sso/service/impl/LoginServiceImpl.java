package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.List;
import java.util.UUID;
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private  Integer SESSION_EXPIRE;

    /**
     *  用户是否可以登陆的判断 可以返回false 也可以返回true
     * @param username
     * @param password
     * @return
     */
    @Override
    public E3Result userLogin(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return E3Result.build(400, "用户名或密码不能为空");
        }
        //查询用户名
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> userList = userMapper.selectByExample(example);
        //如果无该用户
        if (userList == null || userList.size() == 0) {
            return E3Result.build(400, "用户名或密码错误");
        }
        TbUser user = userList.get(0);
        //判断密码是否相同  要多用equals 而不是 ==
        String md5passwd = DigestUtils.md5DigestAsHex(password.getBytes());
        if ( !  user.getPassword().equals(md5passwd )) {
            return E3Result.build(400, "用户名或密码错误");
        }
        //都判断正确之后保存token cookie是在表现层中添加的
        String token = UUID.randomUUID().toString();
        user.setPassword(null);//避免密码的出现
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
        //模仿session  需要 设置过期时间
        jedisClient.expire("SESSION:" + token,SESSION_EXPIRE);
        return E3Result.ok(token);
    }
}
