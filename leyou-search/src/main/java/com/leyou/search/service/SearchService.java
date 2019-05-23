package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.item.BrandClient;
import com.leyou.search.client.item.CategoryClient;
import com.leyou.search.client.item.GoodsClient;
import com.leyou.search.client.item.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 定义分类和品牌的聚合名称
    private static final String CATEGORY_AGG_NAME = "category";

    private static final String BRAND_AGG_NAME = "brand";

    /**
     * 构建goods搜素对象
     *
     * @param spu
     * @return
     * @throws IOException
     */
    public Goods buildGoods(Spu spu) throws IOException {

        Goods goods = new Goods();

        /* -----------------根据spu直接设置goods某些字段-------------------- */

        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setCreateTime(spu.getCreateTime());

        /* -----------------设置搜索字段:标题+分类名称+品牌名称---------------- */

        List<String> nameList = categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        String brandName = brandClient.queryBrandById(spu.getBrandId()).getName();

        goods.setAll(spu.getTitle().concat(StringUtils.join(nameList, " ")).concat(brandName));

        /* -----------------设置sku的集合和设置sku价格的集合------------------------------- */

        List<Sku> skuList = goodsClient.querySkuBySpuId(spu.getId());

        // 创建价格的集合
        List<Long> priceList = new ArrayList<>();
        // 创建sku的集合(只取sku中的一部分字段)
        List<Map<String, Object>> subSkuList = new ArrayList<>();

        skuList.forEach(sku -> {
            // 设置价格
            priceList.add(sku.getPrice());

            // 设置sku部分字段
            Map<String, Object> subSkuMap = new HashMap<>();
            subSkuMap.put("id", sku.getId());
            subSkuMap.put("title", sku.getTitle());
            subSkuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            subSkuMap.put("price", sku.getPrice());

            subSkuList.add(subSkuMap);
        });

        goods.setPrice(priceList);

        goods.setSkus(MAPPER.writeValueAsString(subSkuList));

        /* -----------------设置搜索规格参数------------------------- */

        List<SpecParam> paramsList = specificationClient.querySpecParamsByCdi(null, spu.getCid3(), true, null);

        SpuDetail spuDetail = goodsClient.querySpuDetailById(spu.getId());

        // 获取通用的规格参数  反序列化为map
        Map<String, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {
        });
        // 获取特殊的规格参数  反序列化为map
        Map<String, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {
        });

        Map<String, Object> specMap = new HashMap<>();

        paramsList.forEach(param -> {
            if (param.getGeneric()) {
                String value = genericSpecMap.get(param.getId().toString()).toString();
                if (param.getNumeric()) {
                    value = chooseSegment(value, param);
                }
                specMap.put(param.getName(), value);
            } else {
                specMap.put(param.getName(), specialSpecMap.get(param.getId().toString()));
            }
        });

        goods.setSpecs(specMap);

        return goods;
    }

    /**
     * 转换数值范围
     *
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 根据搜索条件进行搜索
     *
     * @param searchRequest
     * @return
     */
    public PageResult<Goods> search(SearchRequest searchRequest) {

        if (searchRequest == null || StringUtils.isBlank(searchRequest.getKey()))
            return null;

        // 创建自定义查询对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 添加搜索条件 -- 根据all字段进行match匹配查询  逻辑关系是and
        MatchQueryBuilder baseQueryBuilder = QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND);

        /*-----------------------------添加过滤条件-------------------------*/

        // 创建bool查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 将基本查询对象放到bool查询对象中
        boolQueryBuilder.must(baseQueryBuilder);

        // 获取请求对象中的过滤条件
        Map<String, Object> filter = searchRequest.getFilter();

        // 遍历filter
        if (!CollectionUtils.isEmpty(filter)) {
            for (Map.Entry<String, Object> entry : filter.entrySet()) {
                String key = entry.getKey();
                // 判断key的类型
                if (StringUtils.equals(key, "品牌")){
                    key = "brandId";
                } else if (StringUtils.equals(key, "分类")){
                    key = "cid3";
                } else{
                    key = "specs." + key + ".keyword";
                }

                boolQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
            }
        }

        // 将bool查询对象构建到本地查询器中
        queryBuilder.withQuery(boolQueryBuilder);

        // 添加结果集过滤 -- 结果集中只需要id subTitle skus
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));

        // 添加分页信息 -- 从searchRequest中获取分页请求数据  索引库中的页码从0开始 所以拿到page后设置分页条件应为page-1
        Integer page = searchRequest.getPage();
        Integer size = searchRequest.getSize();
        queryBuilder.withPageable(PageRequest.of(page - 1, size));

        // 添加分类的聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(CATEGORY_AGG_NAME).field("cid3"));
        // 添加品牌的集合
        queryBuilder.addAggregation(AggregationBuilders.terms(BRAND_AGG_NAME).field("brandId"));

        // 执行搜索 -- 获取结果集
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());

        // 解析聚合结果集 -- 分类
        List<Map<String, Object>> categories = (List<Map<String, Object>>) getAggResult(goodsPage, CATEGORY_AGG_NAME);
        // 解析聚合结果集 -- 品牌
        List<Brand> brands = (List<Brand>) getAggResult(goodsPage, BRAND_AGG_NAME);

        /*--------获取规格参数的聚合结果集的解析---------*/
        List<Map<String, Object>> specs = null;

        // 根据具体某一个分类和基本查询条件进行规格参数的聚合
        if (categories != null && categories.size() == 1) {
            specs = getSpecs((Long) categories.get(0).get("id"), baseQueryBuilder);
        }

        return new SearchResult(goodsPage.getTotalElements(), goodsPage.getContent(), goodsPage.getTotalPages(), categories, brands, specs);
    }

    /**
     * 获取规格参数的聚合结果集
     *
     * @param id
     * @param baseQueryBuilder
     * @return
     */
    private List<Map<String, Object>> getSpecs(Long id, MatchQueryBuilder baseQueryBuilder) {

        // 查询要添加聚合的规格参数
        List<SpecParam> specParams = specificationClient.querySpecParamsByCdi(null, id, true, null);

        // 创建自定义查询对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 添加搜索条件
        queryBuilder.withQuery(baseQueryBuilder);

        // 添加结果集过滤 -- 不需要任何结果集
        queryBuilder.withSourceFilter(new FetchSourceFilter(null, null));

        // 添加规格参数聚合
        specParams.forEach(specParam -> {
            String name = specParam.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        });

        // 执行查询
        AggregatedPage<Goods> goods = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());

        /*--------解析聚合结果集--------*/

        // 创建返回值数据
        List<Map<String, Object>> specs = new ArrayList<>();
        // 根据结果集获取所有的聚合(map) key是聚合名 value是聚合对象
        Map<String, Aggregation> aggregationMap = goods.getAggregations().asMap();
        // 遍历聚合map
        for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
            // 创建返回项
            Map<String, Object> map = new HashMap<>();
            // 添加k
            map.put("k", entry.getKey());
            // 添加options 1，将聚合对象强转为具体某类聚合 2，获取聚合中所有bucket中的key 转为list集合
            StringTerms terms = (StringTerms) entry.getValue();
            map.put("options", terms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList()));
            specs.add(map);
        }

        return specs;
    }

    /**
     * 解析聚合结果集
     *
     * @param goodsPage
     * @param aggName
     * @return
     */
    private Object getAggResult(AggregatedPage<Goods> goodsPage, String aggName) {


        try {
            if (CATEGORY_AGG_NAME.equals(aggName)) {
                // 强转为LongTerms
                LongTerms categoryAgg = (LongTerms) goodsPage.getAggregation(aggName);
                // 解析出cid 的集合
                List<Long> cids = categoryAgg.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(cids))
                    return new ArrayList<>();
                // 根据cids 查询分类的名称集合
                List<String> names = categoryClient.queryNamesByIds(cids);
                // 构建分类的信息集合 List<Map<String, Object>>
                List<Map<String, Object>> categories = new ArrayList<>();

                for (int i = 0; i < names.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", cids.get(i));
                    map.put("name", names.get(i));
                    categories.add(map);
                }
                return categories;
            }

            if (BRAND_AGG_NAME.equals(aggName)) {
                // 强转为LongTerms
                LongTerms brandAgg = (LongTerms) goodsPage.getAggregation(aggName);
                // 解析出brand 的集合
                List<Long> brandIds = brandAgg.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(brandIds))
                    return new ArrayList<>();
                // 根据brandIds 查询Brand 集合
                List<Brand> brands = brandClient.queryBrandsByIds(brandIds);

                return brands;
            }
            LOGGER.info("聚合名称无对应的聚合结果集 不解析 返回null");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("解析聚合结果集出现异常 返回null");
            return null;
        }
    }

    /**
     * 根据id 创建索引
     * @param id
     */
    public void createIndex(Long id) throws IOException {

        // 查询spu
        Spu spu = goodsClient.querySpuBySpuId(id);

        // 构建goods对象
        Goods goods = buildGoods(spu);

        // 保存到索引库
        goodsRepository.save(goods);
    }

    /**
     * 根据id 删除索引
     * @param id
     */
    public void deleteIndex(Long id) {

        goodsRepository.deleteById(id);
    }
}
