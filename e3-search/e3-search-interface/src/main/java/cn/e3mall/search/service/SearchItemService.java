package cn.e3mall.search.service;

import cn.e3mall.common.utils.E3Result;

/**
 * @author XYQ
 */
public interface SearchItemService {
    /**
     * 导入所有商品到索引库中
     *
     * @return
     */
    E3Result importAllItems();
}
