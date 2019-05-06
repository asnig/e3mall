package cn.e3mall.controller;

import cn.e3mall.common.pojo.TbItemParam;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author XYQ
 */
@Controller
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping("/rest/item/param/item/query/{id}")
    @ResponseBody
    public E3Result queryParam(@PathVariable("id") long id) {
        System.out.println(id);
        TbItemParam itemParam = itemParamService.queryItemParamById(id);
        return E3Result.ok(itemParam);
    }
}
