package com.leyou.item.api.outer;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {

    /**
     * 根据条件分页查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("page")
    public PageResult<Brand> queryBrandsByPage(
                                            @RequestParam(value = "key", required = false) String key,
                                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                            @RequestParam(value = "desc", required = false) Boolean desc);


    /**
     * 根据品牌的id 查询品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id") Long id);

    /**
     * 根据品牌id 集合查询品牌集合
     * @param ids
     * @return
     */
    @GetMapping("ids")
    public List<Brand> queryBrandsByIds(@RequestParam("ids") List<Long> ids);
}
