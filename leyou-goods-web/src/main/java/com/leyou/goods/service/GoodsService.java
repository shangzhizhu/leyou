package com.leyou.goods.service;

import com.leyou.goods.client.item.BrandClient;
import com.leyou.goods.client.item.CategoryClient;
import com.leyou.goods.client.item.GoodsClient;
import com.leyou.goods.client.item.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    /**
     * 根据spuId 加载商品详情页面的数据
     * 将各个对象以map的形式返回 key是对象名称 value是具体的对象或者对象集合
     * @param spuId
     * @return
     */
    public Map<String, Object> loadData(Long spuId){

        Map<String, Object> resultMap = new HashMap<>();

        // --- 封装spu

        Spu spu = goodsClient.querySpuBySpuId(spuId);
        resultMap.put("spu", spu);

        // --- 封装spuDetail

        SpuDetail spuDetail = goodsClient.querySpuDetailById(spuId);
        resultMap.put("spuDetail", spuDetail);

        // --- 封装categories  key是"categories" value应该是一个List<Map<String, Object>>--list中的map存放"id"-id "name"-name键值对

        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = categoryClient.queryNamesByIds(cids);

        List<Map<String, Object>> categoryMapList = new ArrayList<>();

        for (int i = 0; i < cids.size(); i++) {

            Map<String, Object> categoryMap = new HashMap<>();

            categoryMap.put("id", cids.get(i));
            categoryMap.put("name", names.get(i));

            categoryMapList.add(categoryMap);
        }

        resultMap.put("categories", categoryMapList);

        // --- 封装brand

        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        resultMap.put("brand", brand);

        // --- 封装skus

        List<Sku> skus = goodsClient.querySkuBySpuId(spuId);
        resultMap.put("skus", skus);

        // --- 封装groups(内置params)

        List<SpecGroup> groups = specificationClient.querySpecGroupsAndParamsByCid(spu.getCid3());
        resultMap.put("groups", groups);

        // --- 封装特殊规格参数paramMap<id, name>

        List<SpecParam> params = specificationClient.querySpecParamsByCdi(null, spu.getCid3(), null, false);
        Map<Long, String> paramMap = new HashMap<>();

        params.forEach(param -> paramMap.put(param.getId(), param.getName()));
        resultMap.put("paramMap", paramMap);


        return resultMap;
    }
}
