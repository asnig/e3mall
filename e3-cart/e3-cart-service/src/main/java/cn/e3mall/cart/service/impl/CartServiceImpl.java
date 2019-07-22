package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.TbItem;
import cn.e3mall.common.redis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XYQ
 */
@Service

public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_CART_PRE}")
    private String redisCartPre;

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 把购物车添加到Redis中
     *
     * @param userId 用户id
     * @param itemId 商品id
     * @param num    商品数量
     * @return E3Result
     */
    @Override
    public E3Result addCartToRedis(Long userId, Long itemId, Integer num) {
        // 查询redis中是否存在商品
        Boolean hexists = jedisClient.hexists(redisCartPre + ":" + userId, itemId + "");
        // 商品存在，商品数量相加
        if (hexists) {
            String json = jedisClient.hget(redisCartPre + ":" + userId, itemId + "");
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            assert item != null;
            item.setNum(num + item.getNum());
            jedisClient.hset(redisCartPre + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
            return E3Result.ok();
        }
        // 商品不存在，查询商品信息，写入redis
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        item.setNum(num);
        String image = item.getImage();
        if (StringUtils.isNotBlank(image)) {
            item.setImage(image.split(",")[0]);
        }
        jedisClient.hset(redisCartPre + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return E3Result.ok();
    }

    /**
     * 合并cookie中的和用户所有的购物车
     *
     * @param userId         用户id
     * @param cookieItemList 从cookie中获取的购物车
     * @return E3Result
     */
    @Override
    public E3Result mergeCart(Long userId, List<TbItem> cookieItemList) {
        for (TbItem item : cookieItemList) {
            addCartToRedis(userId, item.getId(), item.getNum());
        }
        return E3Result.ok();
    }

    /**
     * 获取用户的购物车列表
     *
     * @param userId 用户id
     * @return 购物车列表
     */
    @Override
    public List<TbItem> getCartList(Long userId) {
        // 获取保存在redis中的用户的购物车列表
        List<String> jsonList = jedisClient.hvals(redisCartPre + ":" + userId);
        List<TbItem> itemList = new ArrayList<>();
        for (String json : jsonList) {
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            itemList.add(item);
        }
        return itemList;
    }

    /**
     * 更新购物车中商品的数量
     *
     * @param userId 用户id
     * @param itemId 商品id
     * @param num    需要更新的数量
     * @return E3Result
     */
    @Override
    public E3Result updateItemNum(Long userId, Long itemId, Integer num) {
        // 从redis中获取指定商品
        String s = jedisClient.hget(redisCartPre + ":" + userId, itemId + "");
        TbItem item = JsonUtils.jsonToPojo(s, TbItem.class);
        assert item != null;
        // 设置数量
        item.setNum(num);
        jedisClient.hset(redisCartPre + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return E3Result.ok();

    }

    /**
     * 删除购物车中的商品
     *
     * @param userId 用户id
     * @param itemId 商品id
     * @return E3Result
     */
    @Override
    public E3Result deleteCartItem(Long userId, Long itemId) {
        Long result = jedisClient.hdel(redisCartPre + ":" + userId, itemId + "");
        return E3Result.ok();
    }
}
