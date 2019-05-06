package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.pojo.TbItemCat;
import cn.e3mall.common.pojo.TbItemCatExample;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XYQ
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper mapper;

    @Override
    public List<EasyUITreeNode> getNodes(long parentId) {
        List<EasyUITreeNode> result = new ArrayList<>();

        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria().andParentIdEqualTo(parentId);
        List<TbItemCat> list = mapper.selectByExample(example);
        for (TbItemCat tbItemCat : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent() ? "closed" : "open");
            result.add(node);
        }
        return result;
    }
}
