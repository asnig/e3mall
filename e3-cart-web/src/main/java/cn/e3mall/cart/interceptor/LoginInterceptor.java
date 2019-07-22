package cn.e3mall.cart.interceptor;

import cn.e3mall.common.pojo.TbUser;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录拦截器
 *
 * @author XYQ
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final int EXPIRE_CODE = 201;
    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中取token
        String token = CookieUtils.getCookieValue(request, "token", true);
        // 没有token，未登录。放行
        if (StringUtils.isBlank(token)) {
            return true;
        }
        // 有token，调用sso服务判断是否session过期
        E3Result result = tokenService.getUserByToken(token);
        // 过期。未登录。放行
        if (result.getStatus() == EXPIRE_CODE) {
            return true;
        }
        // 没有过期。把用户信息放入request域中。放行
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user", user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {

    }
}
