package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.pojo.TbContentCategory;
import cn.e3mall.common.pojo.TbContentCategoryExample;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author XYQ
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCatList(long parentId) {
        //根据parentId获取子结点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> categoryList = contentCategoryMapper.selectByExample(example);
        List<EasyUITreeNode> nodeList = new ArrayList<>();
        for (TbContentCategory category : categoryList) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(category.getId());
            node.setText(category.getName());
            node.setState(category.getIsParent() ? "closed" : "open");
            nodeList.add(node);

        }
        return nodeList;
    }

    @Override
    public E3Result createContentCat(long parentId, String name) {
        //在parentId下创建子结点
        TbContentCategory cat = new TbContentCategory();
        cat.setCreated(new Date());
        cat.setUpdated(new Date());
        cat.setParentId(parentId);
        cat.setName(name);
        cat.setIsParent(false);
        cat.setSortOrder(1);
        //1(正常)2(删除)
        cat.setStatus(1);
        contentCategoryMapper.insertSelective(cat);
        EasyUITreeNode node = new EasyUITreeNode();
        node.setId(cat.getId());

        //判断父节点的isParent属性，如果为false，则改为true
        //根据parentId查询父节点
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        return E3Result.ok(node);
    }

    @Override
    public E3Result updateContentCat(long id, String name) {
        TbContentCategory cat = new TbContentCategory();
        cat.setId(id);
        cat.setName(name);
        contentCategoryMapper.updateByPrimaryKeySelective(cat);
        return E3Result.ok();
    }

    @Override
    public E3Result deleteContentCat(long id) {
        //判断该节点下是否还有子节点，如果有，则禁止删除
        TbContentCategoryExample curCatExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = curCatExample.createCriteria();
        criteria.andParentIdEqualTo(id);
        //查询该节点子节点的数量，不为0，则表示还有子节点
        int i = contentCategoryMapper.countByExample(curCatExample);
        if (i != 0) {
            return E3Result.build(100, "该分类下还有子分类,禁止删除!");
        }


        //如果要删除的结点为最后一个结点，则需要更新父节点的isParent字段为false
        TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
        long parentId = category.getParentId();
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria1 = example.createCriteria();
        criteria1.andParentIdEqualTo(parentId);
        int j = contentCategoryMapper.countByExample(example);
        //如果i==1，则说明要删除的节点为父节点的最后一个节点
        if (j==1) {
            //更改父节点的isParent属性为false
            TbContentCategory cat = new TbContentCategory();
            cat.setId(parentId);
            cat.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKeySelective(cat);
        }
        contentCategoryMapper.deleteByPrimaryKey(id);
        return E3Result.ok();
    }
}
