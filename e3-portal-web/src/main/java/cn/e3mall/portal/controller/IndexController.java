package cn.e3mall.portal.controller;

import cn.e3mall.common.pojo.TbContent;
import cn.e3mall.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author XYQ
 */
@Controller
public class IndexController {

    @Value("${CONTENT_LUNBO_ID}")
    private long CONTENT_LUNBO_ID;

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String index(Model model) {
        List<TbContent> ad1List = contentService.getContentListByCid(CONTENT_LUNBO_ID);
        model.addAttribute("ad1List",ad1List);
        return "index";
    }
}
