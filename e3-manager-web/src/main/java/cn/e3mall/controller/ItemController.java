package cn.e3mall.controller;

import cn.e3mall.common.pojo.AjaxResult;
import cn.e3mall.common.pojo.TbItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author XYQ
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public AjaxResult<TbItem> getItemList(Integer page, Integer rows) {
        return itemService.getItemList(page, rows);
    }

    @RequestMapping("/item/save")
    @ResponseBody
    public E3Result addItem(TbItem tbItem, String desc) {
        return itemService.addItem(tbItem, desc);
    }

    @RequestMapping("/rest/item/update")
    @ResponseBody
    public E3Result updateItem(TbItem item, String desc) {
        return itemService.updateItem(item, desc);
    }

    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public E3Result deleteItem(String ids) {
        return itemService.deleteItem(ids);
    }
}
