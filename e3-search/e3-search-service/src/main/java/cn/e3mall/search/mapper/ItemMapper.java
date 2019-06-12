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
}
