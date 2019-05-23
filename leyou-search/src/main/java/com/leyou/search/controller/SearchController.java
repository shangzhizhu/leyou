package com.leyou.search.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class SearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private SearchService searchService;

    /**
     * 根据搜索条件进行搜索
     * @param searchRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest){

        LOGGER.info("request-log: (POST) /search  " + searchRequest.toString());

        PageResult<Goods> pageResult = searchService.search(searchRequest);

        if (pageResult == null || CollectionUtils.isEmpty(pageResult.getItems()))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(pageResult);
    }
}
