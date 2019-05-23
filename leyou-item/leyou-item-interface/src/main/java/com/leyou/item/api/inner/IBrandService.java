package com.leyou.item.api.inner;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

public interface IBrandService {

    /**
     * 根据条件分页查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    void saveBrand(Brand brand, List<Long> cids);

    /**
     * 根据3级分类的id 查询品牌
     * @param cid
     * @return
     */
    List<Brand> queryBrandsByCid3(Long cid);

    /**
     * 根据品牌的id 查询品牌
     * @param id
     * @return
     */
    Brand queryBrandById(Long id);

    /**
     * 根据品牌id 集合查询品牌集合
     * @param ids
     * @return
     */
    List<Brand> queryBrandsByIds(List<Long> ids);
}
