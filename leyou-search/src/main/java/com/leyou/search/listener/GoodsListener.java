package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoodsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsListener.class);

    @Autowired
    private SearchService searchService;

    /**
     * 监听商品的新增和修改消息，新增或者修改对应商品的搜索索引
     * @param id
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.CREATE.INDEX.QUEUE", durable = "true"),
            exchange = @Exchange(
                    value = "LEYOU.ITEM.EXCHANGE",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.insert", "item.update"}
    ))
    public void listenCreate(Long id) throws IOException {

        if (id == null){
            LOGGER.info("监听商品消息，商品id为null");
            return;
        }
        // 添加索引
        searchService.createIndex(id);

    }

    /**
     * 监听商品的删除消息，删除对应商品的索引
     * @param id
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.DELETE.INDEX.QUEUE", durable = "true"),
            exchange = @Exchange(
                    value = "LEYOU.ITEM.EXCHANGE",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.delete"}
    ))
    public void listendelete(Long id) throws IOException {

        if (id == null){
            LOGGER.info("监听商品消息，商品id为null");
            return;
        }
        // 删除索引
        searchService.deleteIndex(id);

    }
}
