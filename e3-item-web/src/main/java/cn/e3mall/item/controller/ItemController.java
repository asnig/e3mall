package cn.e3mall.item.controller;

import cn.e3mall.common.pojo.TbItem;
import cn.e3mall.common.pojo.TbItemDesc;
import cn.e3mall.item.pojo.Item;
import cn.e3mall.service.ItemDescService;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品详情页Controller
 *
 * @author XYQ
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemDescService itemDescService;

    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable long itemId, Model model) {
        TbItem tbItem = itemService.getItemById(itemId);
        Item item = new Item(tbItem);
        TbItemDesc tbItemDesc = itemDescService.queryTbItemDescById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", tbItemDesc);
        return "item";
    }
}
