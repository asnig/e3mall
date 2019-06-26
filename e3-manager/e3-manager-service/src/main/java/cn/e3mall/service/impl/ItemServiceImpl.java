package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.AjaxResult;
import cn.e3mall.common.pojo.TbItem;
import cn.e3mall.common.pojo.TbItemDesc;
import cn.e3mall.common.pojo.TbItemExample;
import cn.e3mall.common.redis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

/**
 * @author XYQ
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination topicDestination;

    @Autowired
    private JedisClient jedisClient;

    /**
     * redis中商品key的前缀
     */
    @Value("${REDIS_ITEM_PRE}")
    private String redisItemPre;

    /**
     * item在redis中的过期时间
     */
    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer itemCacheExpire;

    @Override
    public TbItem getItemById(Long itemId) {
        // 查询缓存
        try {
            String json = jedisClient.get(redisItemPre + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json, TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);

        try {
            // 把商品放入redis缓存中
            jedisClient.set(redisItemPre + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
            // 设置过期时间
            jedisClient.expire(redisItemPre + ":" + itemId + ":BASE", itemCacheExpire);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tbItem;
    }

    @Override
    public AjaxResult<TbItem> getItemList(Integer page, Integer rows) {
        TbItemExample example = new TbItemExample();
        PageHelper.startPage(page, rows);
        List<TbItem> list = itemMapper.selectByExample(example);
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        AjaxResult<TbItem> result = new AjaxResult<>();
        result.setTotal(pageInfo.getTotal());
        result.setRows(pageInfo.getList());
        return result;
    }

    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setItemDesc(desc);
        itemMapper.insert(tbItem);
        tbItemDescMapper.insert(tbItemDesc);

        // 发送消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(itemId + "");
            }
        });

        return E3Result.ok();
    }

    @Override
    public E3Result updateItem(TbItem tbItem, String desc) {
        itemMapper.updateByPrimaryKeySelective(tbItem);
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
        return E3Result.ok();
    }

    @Override
    public E3Result deleteItem(String ids) {
        String[] idss = ids.split(",");
        for (String id : idss) {
            itemMapper.deleteByPrimaryKey(Long.valueOf(id));
        }
        return E3Result.ok();
    }

    /**
     * 通过商品id获取商品描述
     *
     * @param itemId 商品id
     * @return 商品描述
     */
    @Override
    public TbItemDesc getTbItemDescByItemId(long itemId) {

// 查询缓存
        try {
            String json = jedisClient.get(redisItemPre + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json, TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        try {
            // 把商品放入redis缓存中
            jedisClient.set(redisItemPre + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            // 设置过期时间
            jedisClient.expire(redisItemPre + ":" + itemId + ":DESC", itemCacheExpire);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItemDesc;
    }

}
