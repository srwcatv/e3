package com.catv.sso.service;

import com.catv.common.pojo.E3Result;
import com.catv.pojo.TbUser;

/**
 * 用户
 */
public interface UserService {

    /**
     * 注册信息校验
     * @param param 参数
     * @param type 类型
     * @return
     */
    E3Result checkData(String param, int type);

    /**
     * 创建用户
     * @param user
     * @return
     */
    E3Result createUser(TbUser user);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    E3Result login(String username, String password);

    /**
     * 通过token获得用户信息
     * @param token
     * @return
     */
    E3Result getUserByToken(String token);
}
