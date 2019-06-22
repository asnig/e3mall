package cn.e3mall.service;

import cn.e3mall.common.pojo.TbItemDesc;

/**
 * @author XYQ
 */
public interface ItemDescService {
    /**
     * 根据商品id查询商品描述
     *
     * @param id 商品id
     * @return TbItemDesc
     */
    TbItemDesc queryTbItemDescById(long id);
}
