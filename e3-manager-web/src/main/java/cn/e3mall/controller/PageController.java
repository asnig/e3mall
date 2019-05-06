package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转控制器
 *
 * @author XYQ
 */
@Controller
public class PageController {
    @RequestMapping("/")
    public String toIndex() {
        return "index";
    }

    @RequestMapping("/{page}")
    public String toPage(@PathVariable String page) {
        return page;
    }

    @RequestMapping("/rest/page/item-edit")
    public String toEdit() {
        return "item-edit";
    }

}
