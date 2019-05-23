package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.api.inner.IGoodsService;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService implements IGoodsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsService.class);

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailsMapper spuDetailsMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 分页查询商品spu
     * @param key
     * @param page
     * @param rows
     * @param saleable
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuByPage(String key, Integer page, Integer rows, Boolean saleable) {

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        // 设置根据title模糊查询
        if (StringUtils.isNotBlank(key))
            criteria.andLike("title", "%" + key + "%");

        // 设置根据是否上下架查询
        if (saleable != null)
            criteria.andEqualTo("saleable", saleable);

        // 开启分页查询,每页最多查100条
        PageHelper.startPage(page, Math.min(rows, 100));

        List<Spu> spuList = spuMapper.selectByExample(example);

        List<SpuBo> spuBoList = new ArrayList<>();

        spuList.forEach( spu -> {

            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);

            // 查询品牌,设置品牌名
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());

            // 查询分类,1 2 3 级分类都要查
            List<Category> categoryList = categoryMapper.selectByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            // 处理分类集合返回一个新的集合  1.8 语法: list.stream().map( /*处理逻辑*/ ).collect(Collectors.toList())
            List<String> cnameList = categoryList.stream().map(category -> category.getName()).collect(Collectors.toList());
            // 将名称之间用"-"链接
            String cname = StringUtils.join(cnameList, "-");
            spuBo.setCname(cname);

            spuBoList.add(spuBo);

        } );

        return new PageResult<>(new PageInfo<>(spuList).getTotal(), spuBoList);
    }

    /**
     * 新增商品
     * @param spuBo
     */
    @Transactional
    @Override
    public void saveGoods(SpuBo spuBo) {

        /* ---------添加spu--------- */
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuBo, spu);
        // 设置上架
        spu.setSaleable(true);
        // 设置有效
        spu.setValid(true);
        // 设置创建，更新时间
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());

        spuMapper.insertSelective(spu);

        /* ---------添加spuDetails--------- */
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailsMapper.insertSelective(spuDetail);

        saveSkuAndStock(spuBo, spu.getId());

        // 发送商品添加的消息
        sendMessage("insert", spuBo.getId());
    }

    /**
     * 更新商品
     * @param spuBo
     * @return
     */
    @Override
    public void updateGoods(SpuBo spuBo) {

        // 查询sku
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        List<Sku> skuList = skuMapper.select(sku);
        // 转化id 集合
        List<Long> skuIds = skuList.stream().map(skuItem -> skuItem.getId()).collect(Collectors.toList());

        // 删除stock
        Example stockExamlpe = new Example(Stock.class);
        stockExamlpe.createCriteria().andIn("skuId", skuIds);
        stockMapper.deleteByExample(stockExamlpe);

        // 删除sku
        skuMapper.delete(sku);

        // 更新spu
        spuBo.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKeySelective(spuBo);

        // 更新spuDetail
        spuDetailsMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        saveSkuAndStock(spuBo, spuBo.getId());

        // 发送商品更新的消息
        sendMessage("update", spuBo.getId());

    }

    /**
     * 根据spuId查询spu
     * @param id
     * @return
     */
    @Override
    public Spu querySpuBySpuId(Long id) {

        return spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据skuId 查询sku
     * @param skuId
     * @return
     */
    @Override
    public Sku querySkuBySkuId(Long skuId) {

        return skuMapper.selectByPrimaryKey(skuId);
    }


    /**
     * 保存sku和stock
     * @param spuBo
     * @param spuId
     */
    private void saveSkuAndStock(SpuBo spuBo, Long spuId) {
        /* ---------添加sku--------- */
        spuBo.getSkus().forEach(sku -> {

            sku.setSpuId(spuId);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insertSelective(sku);

            /* ---------添加stock--------- */
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据spuid 查询spuDetail
     * @param id
     * @return
     */
    @Override
    public SpuDetail querySpuDetailById(Long id) {

        return spuDetailsMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据spuId 查询sku
     * @param spuId
     * @return
     */
    @Override
    public List<Sku> querySkuBySpuId(Long spuId) {

        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        // 查询库存
        skuList.forEach(skuItem -> {
            Stock stock = stockMapper.selectByPrimaryKey(skuItem.getId());
            skuItem.setStock(stock.getStock());
        });
        return skuList;
    }

    /**
     * 发送商品的消息
     * @param type
     * @param id
     */
    private void sendMessage(String type, Long id){

        try {
            amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("{}商品消息发送异常, 商品id:{}", type, id, e);
        }

    }

}
