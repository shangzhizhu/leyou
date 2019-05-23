package com.leyou.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsListener.class);

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsProperties smsProperties;

    /**
     * 监听发验证码短信的消息 发送短信
     * @param map
     * @throws ClientException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU-SMS-VERIFYCODE-QUEUE", durable = "true"),
            exchange = @Exchange(
                    value = "LEYOU-SMS-EXCHANGE",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"sms.verifycode"}
    ))
    public void sendMsg(Map<String, String> map) throws ClientException {

        if (CollectionUtils.isEmpty(map))
            return;

        String phone = map.get("phone");

        String code = map.get("code");

        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code))
            return;


        LOGGER.info("监听验证码的消息内容:" + map.toString());

        // 发送短信验证码
        //smsUtils.sendSms(phone, code, smsProperties.getSignName(), smsProperties.getVerifyCodeTemplate());
    }
}
