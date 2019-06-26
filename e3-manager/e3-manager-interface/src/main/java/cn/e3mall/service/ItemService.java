package cn.e3mall.service;


import cn.e3mall.common.pojo.AjaxResult;
import cn.e3mall.common.pojo.TbItem;
import cn.e3mall.common.pojo.TbItemDesc;
import cn.e3mall.common.utils.E3Result;

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

    /**
     * 添加商品
     *
     * @param tbItem
     * @param desc
     * @return
     */
    E3Result addItem(TbItem tbItem, String desc);

    /**
     * 修改商品
     *
     * @param tbItem
     * @param desc
     * @return
     */
    E3Result updateItem(TbItem tbItem, String desc);

    /**
     * 删除商品
     */
    E3Result deleteItem(String ids);

    /**
     * 通过商品id获取商品描述
     * @param itemId
     * @return
     */
    TbItemDesc getTbItemDescByItemId(long itemId);
}
