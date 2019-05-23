package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.api.inner.IBrandService;
import com.leyou.item.pojo.Brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandController {

    private static final Logger logger = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private IBrandService brandService;

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
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
                                            @RequestParam(value = "key", required = false) String key,
                                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                            @RequestParam(value = "desc", required = false) Boolean desc){

        logger.info("request-log:-----> (GET) brand/page?key="+key+"&page="+page+"&rows="+rows+"&sortBy="+sortBy+"&desc="+desc);

        PageResult<Brand> result = brandService.queryBrandsByPage(key, page, rows, sortBy, desc);

        if(CollectionUtils.isEmpty(result.getItems()))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result);
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){

        logger.info("request-log:-----> (POST) brand/  " + brand.toString() + "---" + cids.toString());

        brandService.saveBrand(brand, cids);
        // 几种请求方式成功后返回的响应码 post:201 get:200 put:204 delete:204
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据3级分类的id 查询品牌
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid3(@PathVariable("cid") Long cid){

        logger.info("request-log:-----> (GET) brand/cid/" + cid);

        List<Brand> brandList = brandService.queryBrandsByCid3(cid);

        if(CollectionUtils.isEmpty(brandList))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(brandList);
    }

    /**
     * 根据品牌的id 查询品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){

        logger.info("request-log:-----> (GET) brand/" + id);

        Brand brand = brandService.queryBrandById(id);

        if (brand == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(brand);
    }

    /**
     * 根据品牌id 集合查询品牌集合
     * @param ids
     * @return
     */
    @GetMapping("ids")
    public ResponseEntity<List<Brand>> queryBrandsByIds(@RequestParam("ids") List<Long> ids){

        logger.info("request-log:-----> (GET) brand/ids/ids?=" + ids);

        List<Brand> brands = brandService.queryBrandsByIds(ids);

        if (CollectionUtils.isEmpty(brands))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(brands);
    }
}
