package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("item")
public class GoodsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @GetMapping("{id}.html")
    public String toItemView(@PathVariable("id") Long id, Model model){

        LOGGER.info("request-log: (GET) item/" + id + ".html");

        // 调用service加载数据
        Map<String, Object> loadDataMap = goodsService.loadData(id);

        // 设置数据模型
        model.addAllAttributes(loadDataMap);

        // 生成静态化页面
        goodsHtmlService.asyncExcute(id);

        // 跳转视图
        return "item";
    }
}
