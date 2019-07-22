package cn.e3mall.cart.service;

import cn.e3mall.common.pojo.TbItem;
import cn.e3mall.common.utils.E3Result;

import java.util.List;

/**
 * @author XYQ
 */
public interface CartService {
    /**
     * 把购物车添加到Redis中
     *
     * @param userId 用户id
     * @param itemId 商品id
     * @param num    商品数量
     * @return E3Result
     */
    E3Result addCartToRedis(Long userId, Long itemId, Integer num);

    /**
     * 合并cookie中的和用户所有的购物车
     *
     * @param userId         用户id
     * @param cookieItemList 从cookie中获取的购物车
     * @return E3Result
     */
    E3Result mergeCart(Long userId, List<TbItem> cookieItemList);

    /**
     * 获取用户的购物车列表
     *
     * @param userId 用户id
     * @return 购物车列表
     */
    List<TbItem> getCartList(Long userId);

    /**
     * 更新购物车中商品的数量
     *
     * @param userId 用户id
     * @param itemId 商品id
     * @param num    需要更新的数量
     * @return E3Result
     */
    E3Result updateItemNum(Long userId, Long itemId, Integer num);

    /**
     * 删除购物车中的商品
     *
     * @param userId 用户id
     * @param itemId 商品id
     * @return E3Result
     */
    E3Result deleteCartItem(Long userId, Long itemId);
}
