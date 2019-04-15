package cn.e3mall.service;


import cn.e3mall.common.pojo.AjaxResult;
import cn.e3mall.common.pojo.TbItem;

/**
 * @author XYQ
 */
public interface ItemService {
    /**
     * 根据id查询Item
     * @param itemId
     * @return TbItem
     */
    TbItem getItemById(Long itemId);

    /**
     * 查询商品列表
     *
     * @param page
     * @param rows
     * @return
     */
    AjaxResult<TbItem> getItemList(Integer page, Integer rows);
}
