package cn.e3mall.sso.service.impl;

import cn.e3mall.common.pojo.TbUser;
import cn.e3mall.common.pojo.TbUserExample;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @author XYQ
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    private static final int USERNAME_TYPE = 1;
    private static final int PHONE_TYPE = 2;
    private static final int EMAIL_TYPE = 3;

    @Autowired
    private TbUserMapper userMapper;

    /**
     * 表单验证
     *
     * @param param 需要校验的数据
     * @param type  校验的数据类型 1、用户名 2、手机号 3、邮箱
     * @return E3Result
     */
    @Override
    public E3Result checkData(String param, int type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        // 根据数据类型的不同设置不同的条件
        if (type == USERNAME_TYPE) {
            criteria.andUsernameEqualTo(param);
        } else if (type == PHONE_TYPE) {
            criteria.andPhoneEqualTo(param);
        } else if (type == EMAIL_TYPE) {
            criteria.andEmailEqualTo(param);
        } else {
            return E3Result.build(400, "数据类型错误");
        }
        // 按条件查询数据库
        List<TbUser> tbUsers = userMapper.selectByExample(example);
        if (tbUsers != null && tbUsers.size() > 0) {
            return E3Result.ok(false);
        }

        return E3Result.ok(true);
    }

    /**
     * 注册用户
     *
     * @param user 用户填写的表单数据封装到user对象中
     * @return E3Result
     */
    @Override
    public E3Result doRegister(TbUser user) {
        // 非空校验
        boolean isBlank = (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
                || StringUtils.isBlank(user.getEmail()));
        if (isBlank) {
            return E3Result.build(400, "用户数据不完整，注册失败！");
        }
        // 后端表单验证
        E3Result result = checkData(user.getUsername(), 1);
        if (!(boolean) result.getData()) {
            return E3Result.build(400, "此用户名已被占用");
        }
        result = checkData(user.getPhone(), 2);
        if (!(boolean) result.getData()) {
            return E3Result.build(400, "此电话号码已被占用");
        }
        // 补全user对象
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //对密码进行md5加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        userMapper.insert(user);
        return E3Result.ok();
    }
}
