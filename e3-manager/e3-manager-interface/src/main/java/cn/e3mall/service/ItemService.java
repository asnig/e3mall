package cn.e3mall.service;


import cn.e3mall.pojo.TbItem;

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
}
