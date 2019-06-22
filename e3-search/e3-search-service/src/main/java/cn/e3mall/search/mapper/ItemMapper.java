package cn.e3mall.search.mapper;

import cn.e3mall.common.pojo.SearchItem;

import java.util.List;

/**
 * @author XYQ
 */
public interface ItemMapper {
    /**
     * 获取ItemList
     *
     * @return
     */
    List<SearchItem> getItemList();

    /**
     * 根据itemId获取SearchItem
     *
     * @param itemId 商品id
     * @return SearchItem对象
     */
    SearchItem getSearchItemByItemId(Long itemId);
}
