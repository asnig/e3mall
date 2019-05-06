package cn.e3mall.service;

import cn.e3mall.common.pojo.TbItemParam;

/**
 * @author XYQ
 */
public interface ItemParamService {
    /**
     * id查询商品描述
     *
     * @param id
     * @return
     */
    TbItemParam queryItemParamById(long id);
}
