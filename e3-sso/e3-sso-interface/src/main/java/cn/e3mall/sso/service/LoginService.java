package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

/**
 * 用户登录Service
 *
 * @author XYQ
 */
public interface LoginService {

    /**
     * 用户登录业务
     *
     * @param username 用户名
     * @param password 密码
     * @return E3Result
     */
    E3Result doLogin(String username, String password);
}
