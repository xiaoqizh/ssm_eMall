package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

//service注解里面的内容就是给他命名的
@Service("myRegister")
public class RegisterServiceImpl  implements RegisterService{

    @Autowired
    private TbUserMapper userMapper;
    //表单校验  ajax判断是否信息有重复的  分类别
    @Override
    public E3Result checkData(String param, Integer type) {
        /* 1 用户名 2 手机号 3 邮箱号*/
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if(type == 1){
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        } else if (type == 3) {
            criteria.andEmailEqualTo(param);
        }else {
            return E3Result.ok(false);
        }
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        //如果有 那就失败
        if (tbUsers != null && tbUsers.size() > 0) {
            return E3Result.ok(false);
        }
        return E3Result.ok(true);
    }


    //传递的是一个表单 表单通常就是pojo接收就行了
    //把注册的信息加载到数据库
    @Override
    public E3Result register(TbUser user) {
        //首先校验是否为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
                || StringUtils.isBlank(user.getPhone())) {
            return E3Result.ok(false);
        }
        //还给在进行一次校验
        E3Result e3Result = checkData(user.getUsername(), 1);
        if(!(Boolean) e3Result.getData()){
            return E3Result.build(400,"用户名已被使用");
        }
        if (!(Boolean) checkData(user.getPhone(), 2).getData()) {
            return E3Result.build(400,"手机号已被使用");
        }
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //对密码进行md5加密
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5DigestAsHex);
        userMapper.insert(user);
        return E3Result.ok(true);
    }
}
