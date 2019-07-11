package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.TbUser;
import cn.e3mall.common.utils.E3Result;

/**
 * 用户注册Service
 *
 * @author XYQ
 */
public interface RegisterService {
    /**
     * 校验注册数据
     *
     * @param param 需要校验的数据
     * @param type  校验的数据类型 1、用户名 2、手机号 3、邮箱
     * @return E3Result
     */
    E3Result checkData(String param, int type);

    /**
     * 注册用户
     *
     * @param user 用户填写的表单数据封装到user对象中
     * @return E3Result
     */
    E3Result doRegister(TbUser user);
}
