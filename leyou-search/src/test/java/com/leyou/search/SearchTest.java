package com.leyou.search;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.SpuBo;
import com.leyou.search.client.item.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchTest {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsRepository repository;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    /**
     * 创建Goods索引库  添加映射
     */
    @Test
    public void createIndex(){

        template.createIndex(Goods.class);
        template.putMapping(Goods.class);

        Integer page = 1;
        Integer rows = 100;

        while (rows == 100){

            PageResult<SpuBo> pageResult = goodsClient.querySpuByPage(null, page, rows, true);

            List<SpuBo> spuBoList = pageResult.getItems();

            page++;
            rows = spuBoList.size();

            List<Goods> goodsList = spuBoList.stream().map(spuBo -> {
                try {
                    return searchService.buildGoods(spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());

            repository.saveAll(goodsList);
        }

    }
}
