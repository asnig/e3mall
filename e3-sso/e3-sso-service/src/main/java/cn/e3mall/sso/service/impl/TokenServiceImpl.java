package cn.e3mall.sso.service.impl;

import cn.e3mall.common.pojo.TbUser;
import cn.e3mall.common.redis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author XYQ
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE_TIME}")
    private Integer sessionExpireTime;

    /**
     * 根据token查询用户
     *
     * @param token 用户token
     * @return E3Result
     */
    @Override
    public E3Result getUserByToken(String token) {
        // 查询redis是否存在token
        String json = jedisClient.get("SESSION:" + token);
        if (StringUtils.isBlank(json)) {
            return E3Result.build(201, "用户信息已经过期！");
        }
        // 重置key的过期时间
        jedisClient.expire("SESSION:" + token, sessionExpireTime);
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return E3Result.ok(user);
    }
}
