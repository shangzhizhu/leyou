package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.api.inner.IGoodsService;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private IGoodsService goodsService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
                                        @RequestParam(value = "key", required = false) String key,
                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                        @RequestParam(value = "saleable", required = false) Boolean saleable){

        LOGGER.info("request-log:-----> (GET) spu/page?key=" + key + "&page=" + page + "&rows=" + rows + "&saleable=" + saleable);

        PageResult<SpuBo> result = goodsService.querySpuByPage(key, page, rows, saleable);

        return ResponseEntity.ok(result);
    }

    /**
     * 新增商品
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){

        LOGGER.info("reqeust-log:-----> (POST) goods  " + spuBo.toString());

        try {
            goodsService.saveGoods(spuBo);

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * 更新商品
     * @param spuBo
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){

        LOGGER.info("reqeust-log:-----> (PUT) goods  " + spuBo.toString());

        try {
            goodsService.updateGoods(spuBo);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * 根据spuid 查询spuDetail
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id){

        LOGGER.info("request-log:-----> (GET) spu/detail/" + id);

        SpuDetail spuDetail = goodsService.querySpuDetailById(id);

        if (spuDetail == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(spuDetail);

    }

    /**
     * 根据spuId 查询sku集合
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long spuId){

        LOGGER.info("request-log:-----> (GET) sku/list?id=" + spuId);

        List<Sku> skuList = goodsService.querySkuBySpuId(spuId);

        if (CollectionUtils.isEmpty(skuList))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(skuList);
    }

    /**
     * 根据spuId 查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuBySpuId(@PathVariable("id") Long id){

        LOGGER.info("request-log:-----> (GET) spu/" + id);

        Spu spu = goodsService.querySpuBySpuId(id);

        if (spu == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(spu);
    }

    /**
     * 根据skuId 查询sku
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    public ResponseEntity<Sku> querySkuBySkuId(@PathVariable("skuId") Long skuId){

        LOGGER.info("request-log: (Get) sku/" + skuId);

        Sku sku = goodsService.querySkuBySkuId(skuId);

        if (sku == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(sku);
    }
}
