package com.leyou.item.api.outer;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据父id查询所有的子节点
     * @param pid
     * @return
     */
    @GetMapping("list")
    public List<Category> queryCategoryListByParentId(@RequestParam("pid") Long pid);

    /**
     * 根据分类的id 查询分类名称
     * @param ids
     * @return
     */
    @GetMapping("names")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);
}
