package com.leyou.item.controller;

import com.leyou.item.api.inner.ICategoryService;
import com.leyou.item.pojo.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private ICategoryService categoryService;

    /**
     * 根据父id查询所有的子节点
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByParentId(@RequestParam("pid") Long pid){

        logger.info("request-log:-----> (GET) category/list?pid=" + pid);

        if (pid == null || pid <0)
            return ResponseEntity.badRequest().build();

        List<Category> categoryList = categoryService.queryCategoryListByParentId(pid);

        if (CollectionUtils.isEmpty(categoryList))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(categoryList);
    }

    /**
     * 根据分类的id 查询分类名称
     * @param ids
     * @return
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids") List<Long> ids){

        if (CollectionUtils.isEmpty(ids)){
            logger.info("[ 根据分类的id 查询分类名称-入参ids不能为空 ]");
            return ResponseEntity.notFound().build();
        }

        logger.info("request-log:-----> (GET) category/names?ids=" + ids.toString());

        List<String> names = categoryService.queryNamesByIds(ids);

        if (CollectionUtils.isEmpty(names))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(names);
    }
}
