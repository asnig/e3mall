package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.TbItemParam;
import cn.e3mall.mapper.TbItemParamMapper;
import cn.e3mall.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author XYQ
 */
@Service
public class ItemParamServiceImpl implements ItemParamService {
    @Autowired
    private TbItemParamMapper itemParamMapper;

    @Override
    public TbItemParam queryItemParamById(long id) {
        System.out.println(itemParamMapper.selectByPrimaryKey(id));
        return itemParamMapper.selectByPrimaryKey(id);
    }
}
