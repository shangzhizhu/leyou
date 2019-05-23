package com.leyou.goods.service;

import com.leyou.common.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;

@Service
public class GoodsHtmlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlService.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private GoodsService goodsService;

    /**
     * 创建html静态页面
     * @param spuId
     */
    public void createHtml(Long spuId){

        PrintWriter writer = null;

        try {
            // 构建数据模型上下文对象context  数据从goodsService的loadData中获取
            Context context = new Context();
            context.setVariables(goodsService.loadData(spuId));

            // 创建输出流并指定输出路径
            File file = new File("F:\\2018\\nginx-1.14.0\\html\\item\\" +spuId + ".html");
            writer = new PrintWriter(file);

            // 模板引擎执行处理生成静态化页面
            templateEngine.process("item", context, writer);

            LOGGER.info("生成静态化" + spuId + ".html 页面");

        } catch (Exception e) {

            e.printStackTrace();
            LOGGER.error("生成静态化" + spuId + ".html 页面出错", e);

        } finally {
            // 关闭流
            if (writer != null)
                writer.close();
        }

    }

    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    public void asyncExcute(Long spuId) {

        ThreadUtils.execute(()->createHtml(spuId));
    }

    /**
     * 根据id 删除静态化页面
     * @param id
     */
    public void deleteHtml(Long id) {

        File file = new File("C:\\project\\nginx-1.14.0\\html\\item\\", id + ".html");
        file.deleteOnExit();
    }
}
