package com.leyou.item.api.inner;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

public interface IGoodsService {

    /**
     * 分页查询商品spu
     * @param key
     * @param page
     * @param rows
     * @param saleable
     * @return
     */
    PageResult<SpuBo> querySpuByPage(String key, Integer page, Integer rows, Boolean saleable);

    /**
     * 新增商品
     * @param spuBo
     */
    void saveGoods(SpuBo spuBo);

    /**
     * 根据spuid 查询spuDetail
     * @param id
     * @return
     */
    SpuDetail querySpuDetailById(Long id);

    /**
     * 根据spuId 查询sku
     * @param spuId
     * @return
     */
    List<Sku> querySkuBySpuId(Long spuId);

    /**
     * 更新商品
     * @param spuBo
     * @return
     */
    void updateGoods(SpuBo spuBo);

    /**
     * 根据spuId查询spu
     * @param id
     * @return
     */
    Spu querySpuBySpuId(Long id);

    /**
     * 根据skuId 查询sku
     * @param skuId
     * @return
     */
    Sku querySkuBySkuId(Long skuId);
}
