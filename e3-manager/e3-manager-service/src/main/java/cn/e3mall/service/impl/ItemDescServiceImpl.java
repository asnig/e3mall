package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.TbItemDesc;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author XYQ
 */
@Service
public class ItemDescServiceImpl implements ItemDescService {
    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Override
    public TbItemDesc queryTbItemDescById(long id) {
        return itemDescMapper.selectByPrimaryKey(id);
    }
}
