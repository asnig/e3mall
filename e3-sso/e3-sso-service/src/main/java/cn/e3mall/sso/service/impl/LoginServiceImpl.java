package cn.e3mall.sso.service.impl;

import cn.e3mall.common.pojo.TbUser;
import cn.e3mall.common.pojo.TbUserExample;
import cn.e3mall.common.redis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author XYQ
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE_TIME}")
    private Integer sessionExpireTime;

    /**
     * 用户登录业务
     *
     * @param username 用户名
     * @param password 密码
     * @return E3Result
     */
    @Override
    public E3Result doLogin(String username, String password) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        // 判断用户名是否正确
        List<TbUser> users = userMapper.selectByExample(example);
        if (users == null || users.size() == 0) {
            // 返回用户名错误
            return E3Result.build(400, "用户名或密码错误!");
        }
        // 获取用户信息
        TbUser tbUser = users.get(0);
        // 判断密码是否正确
        if (DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())) {
            // 返回密码错误
            return E3Result.build(400, "用户名或密码错误！");
        }
        // 生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        // 不能把用户密码放入token
        tbUser.setPassword(null);
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(tbUser));
        // 设置token过期时间
        jedisClient.expire("SESSION:" + token, sessionExpireTime);
        //返回token
        return E3Result.ok(token);
    }
}
