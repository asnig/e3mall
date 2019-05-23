package cn.e3mall.content.service;

import cn.e3mall.common.pojo.AjaxResult;
import cn.e3mall.common.pojo.TbContent;
import cn.e3mall.common.utils.E3Result;

import java.util.List;

/**
 * @author XYQ
 */
public interface ContentService {

    /**
     * 获取指定分类下的内容列表
     * @param categoryId categoryID
     * @param page page
     * @param rows rows
     * @return 内容列表
     */
    AjaxResult<TbContent> getContentList(long categoryId,Integer page, Integer rows);

    /**
     * 添加内容
     * @param content content
     * @return E3Result
     */
    E3Result contentAdd(TbContent content);

    /**
     * 根据cid查询内容列表
     * @param cid cid
     * @return 内容列表
     */
    List<TbContent> getContentListByCid(long cid);
}
