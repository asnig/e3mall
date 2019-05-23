package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 内容分类Controller
 *
 * @author XYQ
 */
@RestController
public class ContentCatController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    public List<EasyUITreeNode> getContentCatList(@RequestParam(value = "id", defaultValue = "0") long parentId) {
        return contentCategoryService.getContentCatList(parentId);
    }

    @RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
    public E3Result createContentCat(long parentId, String name) {
        return contentCategoryService.createContentCat(parentId, name);
    }

    @RequestMapping("/content/category/update")
    public E3Result updateContentCat(long id, String name) {
        return contentCategoryService.updateContentCat(id, name);
    }

    @RequestMapping("/content/category/delete")
    public E3Result deleteContentCat(long id) {
        return contentCategoryService.deleteContentCat(id);
    }

}
