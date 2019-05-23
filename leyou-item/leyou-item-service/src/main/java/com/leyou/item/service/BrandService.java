package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.api.inner.IBrandService;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;


    /**
     * 根据条件分页查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        // 创建条件查询对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        // 添加key的搜索条件
        if (StringUtils.isNotBlank(key))
            criteria.andLike("name", "%" + key +key).orEqualTo("letter", key);

        // 添加排序的条件
        if (StringUtils.isNotBlank(sortBy))
            example.setOrderByClause(sortBy + (desc ? " DESC" : " ASC"));

        // 开启分页查询
        PageHelper.startPage(page, rows);

        // 执行查询  并用分页插件的分页信息对象封装结果集
        List<Brand> brands = brandMapper.selectByExample(example);
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @Transactional
    @Override
    public void saveBrand(Brand brand, List<Long> cids) {

        // 插入brand表
        brandMapper.insertSelective(brand);
        // 主键回显
        Long bid = brand.getId();
        // 插入两个id 到商品分类表与品牌表的中间表
        cids.forEach( cid -> {
            brandMapper.saveCategoryBrand(cid, bid);
        });
    }

    /**
     * 根据3级分类的id 查询品牌
     * @param cid
     * @return
     */
    @Override
    public List<Brand> queryBrandsByCid3(Long cid) {

        return brandMapper.queryBrandsByCid3(cid);
    }

    /**
     * 根据品牌的id 查询品牌
     * @param id
     * @return
     */
    @Override
    public Brand queryBrandById(Long id) {

        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据品牌id 集合查询品牌集合
     * @param ids
     * @return
     */
    @Override
    public List<Brand> queryBrandsByIds(List<Long> ids) {

        return brandMapper.selectByIdList(ids);
    }
}
