package cn.e3mall.controller;

import cn.e3mall.common.pojo.AjaxResult;
import cn.e3mall.common.pojo.TbContent;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理Controller
 *
 * @author XYQ
 */
@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/query/list")
    @ResponseBody
    public AjaxResult<TbContent> getContentList(long categoryId, Integer page, Integer rows) {
        return contentService.getContentList(categoryId, page, rows);
    }

    @RequestMapping("/content/save")
    @ResponseBody
    public E3Result contentAdd(TbContent content){
        return contentService.contentAdd(content);
    }
}
