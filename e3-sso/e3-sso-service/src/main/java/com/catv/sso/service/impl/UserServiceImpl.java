package com.catv.sso.service.impl;

import com.catv.common.pojo.E3Result;
import com.catv.common.utils.JsonUtils;
import com.catv.common.utils.jedis.JedisClient;
import com.catv.mapper.TbUserMapper;
import com.catv.pojo.TbUser;
import com.catv.pojo.TbUserExample;
import com.catv.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 注册校验
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE}")
    private int SESSION_EXPIRE;

    public E3Result checkData(String param, int type) {
        // 1、从tb_user表中查询数据
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        // 2、查询条件根据参数动态生成。
        //1、2、3分别代表username、phone、email
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        } else if (type == 3) {
            criteria.andEmailEqualTo(param);
        } else {
            return E3Result.build(400, "非法的参数");
        }
        //执行查询
        List<TbUser> list = userMapper.selectByExample(example);
        // 3、判断查询结果，如果查询到数据返回false。
        if (list == null || list.size() == 0) {
            // 4、如果没有返回true。
            return E3Result.ok(true);
        }
        // 5、使用E3Result包装，并返回。
        return E3Result.ok(false);
    }

    public E3Result createUser(TbUser user) {
        // 1、使用TbUser接收提交的请求。

        if (StringUtils.isBlank(user.getUsername())) {
            return E3Result.build(400, "用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return E3Result.build(400, "密码不能为空");
        }
        //校验数据是否可用
        E3Result result = checkData(user.getUsername(), 1);
        if (!(boolean) result.getData()) {
            return E3Result.build(400, "此用户名已经被使用");
        }
        //校验电话是否可以
        if (StringUtils.isNotBlank(user.getPhone())) {
            result = checkData(user.getPhone(), 2);
            if (!(boolean) result.getData()) {
                return E3Result.build(400, "此手机号已经被使用");
            }
        }
        //校验email是否可用
        if (StringUtils.isNotBlank(user.getEmail())) {
            result = checkData(user.getEmail(), 3);
            if (!(boolean) result.getData()) {
                return E3Result.build(400, "此邮件地址已经被使用");
            }
        }
        // 2、补全TbUser其他属性。
        user.setCreated(new Date());
        user.setUpdated(new Date());
        // 3、密码要进行MD5加密。
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        // 4、把用户信息插入到数据库中。
        userMapper.insert(user);
        // 5、返回E3Result。
        return E3Result.ok();

    }

    public E3Result login(String username, String password) {
        // 1、判断用户和密码是否正确
        //根据用户名查询用户信息
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        //执行查询
        List<TbUser> list = userMapper.selectByExample(example);
        if (list == null || list.size() == 0) {
            //返回登录失败
            return E3Result.build(400, "用户名或密码错误");
        }
        //取用户信息
        TbUser user = list.get(0);
        //判断密码是否正确
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            // 2、如果不正确，返回登录失败
            return E3Result.build(400, "用户名或密码错误");
        }
        // 3、如果正确生成token。
        String token = UUID.randomUUID().toString();
        // 4、把用户信息写入redis，key：token value：用户信息
        user.setPassword(null);
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
        // 5、设置Session的过期时间
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        // 6、把token返回
        return E3Result.ok(token);
    }

    public E3Result getUserByToken(String token) {
        //根据token到redis中取用户信息
        String json = jedisClient.get("SESSION:" + token);
        //取不到用户信息，登录已经过期，返回登录过期
        if (StringUtils.isBlank(json)) {
            return E3Result.build(201, "用户登录已经过期");
        }
        //取到用户信息更新token的过期时间
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        //返回结果，E3Result其中包含TbUser对象
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return E3Result.ok(user);
    }
}
