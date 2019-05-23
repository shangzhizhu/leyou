package com.leyou.goods.listener;

import com.leyou.goods.service.GoodsHtmlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsListener.class);

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 监听商品新增和修改的消息，新增或者修改静态化页面
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.CREATE.WEB.QUEUE", durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",
                                ignoreDeclarationExceptions = "true",
                                type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}
    ))
    public void listenCreate(Long id){

        if (id == null){
            LOGGER.info("监听商品消息,商品id为null");
            return;
        }
        // 创建静态化页面
        goodsHtmlService.createHtml(id);
    }

    /**
     * 监听商品删除的消息，删除静态化页面
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.DELETE.WEB.QUEUE", durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void listenDelete(Long id){

        if (id == null){
            LOGGER.info("监听商品消息,商品id为null");
            return;
        }
        // 删除静态化页面
        goodsHtmlService.deleteHtml(id);
    }

}
