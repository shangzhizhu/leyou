package com.leyou.item.api.outer;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(
                                        @RequestParam(value = "key", required = false) String key,
                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                        @RequestParam(value = "saleable", required = false) Boolean saleable);


    /**
     * 根据spuid 查询spuDetail
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    public SpuDetail querySpuDetailById(@PathVariable("id") Long id);

    /**
     * 根据spuId 查询sku集合
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam("id") Long spuId);

    /**
     * 根据spuId 查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public Spu querySpuBySpuId(@PathVariable("id") Long id);

    /**
     * 根据skuId 查询sku
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    public Sku querySkuBySkuId(@PathVariable("skuId") Long skuId);
}
