package cn.e3mall.content.service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;

import java.util.List;

/**
 * @author XYQ
 */
public interface ContentCategoryService {
    /**
     * 根据parentId获取子结点列表
     * @param parentId parentId
     * @return 子结点列表
     */
    List<EasyUITreeNode> getContentCatList(long parentId);

    /**
     * 在parentId下创建子结点
     * @param parentId parentid
     * @param name 新结点的名字
     * @return 创建好的新结点
     */
    E3Result createContentCat(long parentId, String name);

    /**
     * 更新分类名称
     * @param id id
     * @param name 更新后的名称
     * @return E3Result
     */
    E3Result updateContentCat(long id,String name);

    /**
     * 删除指定分类
     * @param id 分类id
     * @return E3Result
     */
    E3Result deleteContentCat(long id);
}
