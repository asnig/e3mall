package cn.e3mall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 注册功能Controller
 *
 * @author XYQ
 */
@Controller
public class RegisterController {

    @RequestMapping("/page/register")
    public String toRegisterPage() {
        return "register";
    }
}
