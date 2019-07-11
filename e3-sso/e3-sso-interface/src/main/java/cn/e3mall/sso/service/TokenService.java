package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

/**
 * ajax请求服务
 *
 * @author XYQ
 */
public interface TokenService {
    /**
     * 根据token查询用户
     *
     * @param token 用户token
     * @return E3Result
     */
    E3Result getUserByToken(String token);
}
