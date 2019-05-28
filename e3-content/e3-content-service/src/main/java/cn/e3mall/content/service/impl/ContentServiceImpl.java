package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.AjaxResult;
import cn.e3mall.common.pojo.TbContent;
import cn.e3mall.common.pojo.TbContentExample;
import cn.e3mall.common.redis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private JedisClient jedisClient;
    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Override
    public AjaxResult<TbContent> getContentList(long categoryId, Integer page, Integer rows) {
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

        jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
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
        try {
            String json = jedisClient.hget(CONTENT_LIST, String.valueOf(cid));
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToList(json, TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExample(example);

        try {
            jedisClient.hset(CONTENT_LIST, String.valueOf(cid),JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
