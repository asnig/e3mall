package cn.e3mall.controller;

import cn.e3mall.common.pojo.TbItemDesc;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品描述控制器
 *
 * @author XYQ
 */
@Controller
public class ItemDescController {
    @Autowired
    private ItemDescService itemDescService;

    @RequestMapping("/rest/item/query/item/desc/{id}")
    @ResponseBody
    public E3Result queryDesc(@PathVariable("id") long id) {
        TbItemDesc tbItemDesc = itemDescService.queryTbItemDescById(id);
        return E3Result.ok(tbItemDesc);
    }
}
