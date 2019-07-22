package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.TbItem;
import cn.e3mall.common.pojo.TbUser;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车Controller
 *
 * @author XYQ
 */
@Controller
public class CartController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    /**
     * 购物车cookie过期时间
     */
    @Value("${CART_COOKIE_EXPIRE_TIME}")
    private Integer cartCookieExpireTime;

    /**
     * 向购物车中添加商品
     *
     * @param itemId   商品id
     * @param num      商品添加的数量，默认为1
     * @param request  request
     * @param response response
     * @return 添加成功后，跳转到cartSuccess.jsp页面
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addToCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                            HttpServletRequest request, HttpServletResponse response) {
        // 判断用户是否登录, 已经登录。把购物车存到redis中
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            // 调用服务把购物车存到redis中
            E3Result result = cartService.addCartToRedis(user.getId(), itemId, num);
            // 返回逻辑视图
            return "cartSuccess";
        }
        // 用户没有登录
        // 获取cookie中的购物车
        List<TbItem> cartList = getCartListFromCookie(request);
        // 查询购物车中是否有同类商品 flag = false表示不存在同类商品，flag = true表示存在
        boolean flag = false;
        for (TbItem item : cartList) {
            // 如果有，则把商品数量相加
            if (item.getId() == itemId.longValue()) {
                flag = true;
                item.setNum(item.getNum() + num);
                break;
            }
        }
        if (!flag) {
            // 如果没有，则从数据库中查询一个tbItem，并放入购物车列表中
            TbItem tbItem = itemService.getItemById(itemId);
            // 补全tbItem
            // 取第一张图片
            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)) {
                tbItem.setImage(image.split(",")[0]);
            }
            // 设置商品数量
            tbItem.setNum(num);
            // 添加商品到购物车列表中
            cartList.add(tbItem);
        }
        // 写入cookie
        CookieUtils.setCookie(request, response, "cart",
                JsonUtils.objectToJson(cartList), cartCookieExpireTime, true);
        return "cartSuccess";
    }

    /**
     * 显示购物车列表
     *
     * @param request 需要从request中获取cookie
     * @return 跳转到购物车列表
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request, HttpServletResponse response, Model model) {
        List<TbItem> cartList = getCartListFromCookie(request);
        // 判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        // 已经登录。
        if (user != null) {
            // 把cookie中的购物车和用户的购物车合并
            cartService.mergeCart(user.getId(), cartList);
            // 删除cookie中的购物车
            CookieUtils.deleteCookie(request, response, "cart");
            // 获取用户的购物车列表(此时购物车列表已经合并)
            cartList = cartService.getCartList(user.getId());
        }
        model.addAttribute("cartList", cartList);
        return "cart";
    }

    /**
     * 更新数量
     *
     * @param itemId   需要更新的商品id
     * @param num      需要更新的数量
     * @param request  从request中获取cookie
     * @param response 通过response写入cookie
     * @return E3Result
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateItemNum(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num,
                                  HttpServletRequest request, HttpServletResponse response) {
        // 判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        // 用户已经登录
        if (user != null) {
            // 对指定商品进行数量修改
            return cartService.updateItemNum(user.getId(), itemId, num);
        }
        List<TbItem> cartList = getCartListFromCookie(request);
        for (TbItem item : cartList) {
            if (item.getId() == itemId.longValue()) {
                item.setNum(num);
                break;
            }
        }
        // 写入cookie
        CookieUtils.setCookie(request, response, "cart",
                JsonUtils.objectToJson(cartList), cartCookieExpireTime, true);
        return E3Result.ok();
    }

    /**
     * 删除购物车中的商品
     *
     * @param itemId 商品id
     * @return 重定向到购物车页面
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String cartDel(@PathVariable("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response) {
        // 判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        // 如果已经登录
        if (user != null) {
            cartService.deleteCartItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }
        List<TbItem> cartList = getCartListFromCookie(request);
        for (TbItem item : cartList) {
            if (item.getId() == itemId.longValue()) {
                cartList.remove(item);
                break;
            }
        }
        CookieUtils.setCookie(request, response, "cart",
                JsonUtils.objectToJson(cartList), cartCookieExpireTime, true);
        return "redirect:/cart/cart.html";
    }

    /**
     * 从cookie中获取购物车列表
     *
     * @param request request
     * @return 购物车列表
     */
    private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, "cart", true);
        // 判断json是否为空
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        // 把json转换为购物车列表从
        return JsonUtils.jsonToList(json, TbItem.class);

    }
}
