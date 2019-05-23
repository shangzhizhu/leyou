package com.leyuou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import com.leyuou.cart.client.GoodsClient;
import com.leyuou.cart.interceptor.LoginInterceptor;
import com.leyuou.cart.pojo.Cart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private static final String LEYOU_CART_PREFIX = "leyou:cart:";

    /**
     * 购物车添加商品
     *
     * 1,从拦截器中获取用户信息
     *
     * 2,从redis中获取用户的购物车(购物车的数据结构是hash,用户id为key)
     *
     * 3,根据skuId判断购物车中是否已有该商品
     *
     * 4,如果已有该商品 则更新商品数量
     *
     * 5,如果无该商品 则调用商品微服务根据skuId查询商品信息并组装购物车中的商品数据
     *
     * 6,重新将购物车写入redis
     * @param cart
     */
    public void addCart(Cart cart) {

        // 获取cart中的信息
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();

        // 查询已有的购物车信息
        BoundHashOperations<String, Object, Object> hashOps = getCartsFromRedis();

        // key:skuId value:cart 判断该商品是否已存在在购物车中
        Boolean flag = hashOps.hasKey(skuId.toString());

        if (flag) {
            // 如果购物车中已存在该商品 则更新数量
            String cartJson = hashOps.get(skuId.toString()).toString();
            // 反序列化为cart对象
            cart = JsonUtils.parse(cartJson, Cart.class);
            // 更新数量
            cart.setNum(cart.getNum() + num);
        } else {
            // 如果购物车中无该商品信息 则根据skuId 查询商品信息并组装cart对象中的数据
            Sku sku = this.goodsClient.querySkuBySkuId(skuId);
            if (sku != null) {
                // 组装购物车中的该商品信息
                cart.setUserId(LoginInterceptor.getThreadLocalUserInfo().getId());
                cart.setImage(StringUtils.isNotBlank(sku.getImages()) ? StringUtils.split(sku.getImages(), ",")[0] : "");
                cart.setOwnSpec(sku.getOwnSpec());
                cart.setPrice(sku.getPrice());
                cart.setTitle(sku.getTitle());
            }
        }
        // 重新将cart写入redis中
        hashOps.put(skuId.toString(), JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车
     * @return
     */
    public List<Cart> queryCarts() {

        // 查询已有的购物车信息
        BoundHashOperations<String, Object, Object> hashOps = getCartsFromRedis();

        // 获取购物车数据
        List<Object> cartJsons = hashOps.values();

        // 解析数据并返回
        return cartJsons.stream().map(cartJson -> JsonUtils.parse(cartJson.toString(), Cart.class)).collect(Collectors.toList());

    }

    /**
     * 修改购物车
     * @param cart
     */
    public void updateCart(Cart cart) {

        // 获取请求中cart的信息
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();

        // 查询已有的购物车信息
        BoundHashOperations<String, Object, Object> hashOps = getCartsFromRedis();

        // 获取对应商品的json数据
        String cartJson = hashOps.get(skuId.toString()).toString();

        // 解析json
        cart = JsonUtils.parse(cartJson, Cart.class);

        // 修改数量
        cart.setNum(num);

        // 重新写入redis购物车中
        hashOps.put(skuId.toString(), JsonUtils.serialize(cart));

    }

    /**
     * 删除购物车中的商品
     * @param skuId
     */
    public void deleteCart(String skuId) {

        // 获取购物车信息
        BoundHashOperations<String, Object, Object> hashOps = getCartsFromRedis();

        // 删除对应skuId的商品
        hashOps.delete(skuId);

    }

    /**
     * 从拦截器中获取用户信息并根据用户信息获取redis中的购物车数据
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartsFromRedis(){

        // 从拦截器中获取用户信息
        UserInfo userInfo = LoginInterceptor.getThreadLocalUserInfo();

        // 组装redis的hash数据结构的key
        String key = LEYOU_CART_PREFIX + userInfo.getId();

        // 查询已有的购物车信息并返回
        return this.redisTemplate.boundHashOps(key);
    }


}
