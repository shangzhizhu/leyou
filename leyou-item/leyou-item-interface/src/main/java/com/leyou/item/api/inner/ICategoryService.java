package com.leyou.item.api.inner;

import com.leyou.item.pojo.Category;

import java.util.List;

public interface ICategoryService {

    /**
     * 根据父id查询所有的子节点
     * @param pid
     * @return
     */
    List<Category> queryCategoryListByParentId(Long pid);

    /**
     * 根据分类id 查询分类名称
     * @param ids
     * @return
     */
    List<String> queryNamesByIds(List<Long> ids);
}
