package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.AjaxResult;
import cn.e3mall.common.pojo.TbContent;
import cn.e3mall.common.pojo.TbContentExample;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author XYQ
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Override
    public AjaxResult<TbContent> getContentList(long categoryId,Integer page, Integer rows) {
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        PageHelper.startPage(page, rows);
        List<TbContent> contentList = contentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(contentList);
        AjaxResult<TbContent> result = new AjaxResult<>();
        result.setTotal(pageInfo.getTotal());
        result.setRows(pageInfo.getList());
        return result;
    }

    /**
     * 添加内容
     *
     * @param content content
     * @return E3Result
     */
    @Override
    public E3Result contentAdd(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insertSelective(content);
        return E3Result.ok();
    }

    /**
     * 根据cid查询内容列表
     *
     * @param cid cid
     * @return 内容列表
     */
    @Override
    public List<TbContent> getContentListByCid(long cid) {
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        return contentMapper.selectByExample(example);
    }
}
