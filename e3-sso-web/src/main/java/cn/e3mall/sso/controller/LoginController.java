package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录处理Controller
 *
 * @author XYQ
 */
@Controller
public class LoginController {

    /**
     * 登录成功状态码
     */
    private static final int LOGIN_SUCCESS_CODE = 200;

    @Autowired
    private LoginService loginService;

    @Value("${TOKEN_KEY_NAME}")
    private String tokenKeyName;

    @RequestMapping("/page/login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public E3Result doLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        E3Result result = loginService.doLogin(username, password);
        if (result.getStatus() == LOGIN_SUCCESS_CODE) {
            // 获取token
            String token = result.getData().toString();
            CookieUtils.setCookie(request, response, tokenKeyName, token);
        }
        return result;
    }
}
