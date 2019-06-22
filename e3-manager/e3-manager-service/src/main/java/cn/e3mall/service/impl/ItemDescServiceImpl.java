package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.TbItemDesc;
import cn.e3mall.common.redis.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.service.ItemDescService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author XYQ
 */
@Service
public class ItemDescServiceImpl implements ItemDescService {
    @Autowired
    private TbItemDescMapper itemDescMapper;

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
    public TbItemDesc queryTbItemDescById(long id) {
        // 查询缓存
        try {
            String json = jedisClient.get(redisItemPre + id + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json, TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(id);

        try {
            // 把商品放入redis缓存中
            jedisClient.set(redisItemPre + ":" + id + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            // 设置过期时间
            jedisClient.expire(redisItemPre + ":" + id + ":DESC", itemCacheExpire);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tbItemDesc;
    }
}
