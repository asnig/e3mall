package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * @author XYQ
 */
public interface ItemCatService {
    List<EasyUITreeNode> getNodes(long parentId);
}
