package com.leyou.item.service;

import com.leyou.item.api.inner.ICategoryService;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父id查询所有的子节点
     * @param pid
     * @return
     */
    @Override
    public List<Category> queryCategoryListByParentId(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    /**
     * 根据分类id 查询分类名称
     * @param ids
     * @return
     */
    @Override
    public List<String> queryNamesByIds(List<Long> ids) {

        return categoryMapper.selectByIdList(ids).stream().map(category -> category.getName()).collect(Collectors.toList());
    }
}
