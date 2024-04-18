package neko.transaction.member.config;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.MQMessageType;
import neko.transaction.commonbase.utils.exception.RabbitMQSendException;
import neko.transaction.member.service.MqReturnMessageService;
import neko.transaction.member.to.RabbitMQChatPubTo;
import neko.transaction.member.to.RabbitMQMessageTo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * rabbitmq消息回退处理配置
 */
@Configuration
@Slf4j
public class RabbitMQAckConfig {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MqReturnMessageService mqReturnMessageService;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if(!ack){
                //ConfirmCallback中回退消息需要在发送消息设置在 CorrelationData 中，如果不设置，需要判断是否为空
                if(correlationData != null && correlationData.getReturned() != null){
                    String message = new String(correlationData.getReturned().getMessage().getBody(), StandardCharsets.UTF_8);
                    RabbitMQMessageTo<Object> rabbitMQMessageTo = JSONUtil.toBean(message,
                            new TypeReference<RabbitMQMessageTo<Object>>() {},
                            true);

                    if(MQMessageType.CHAT_PUB_TYPE.equals(rabbitMQMessageTo.getType())){
                        RabbitMQMessageTo<RabbitMQChatPubTo> mqTo = JSONUtil.toBean(message,
                                new TypeReference<RabbitMQMessageTo<RabbitMQChatPubTo>>() {},
                                true);
                        //获取聊天发布to
                        RabbitMQChatPubTo rabbitMQChatPubTo = mqTo.getMessage();
                        log.error("聊天消息发布队列消息发送至交换机失败，聊天id: " + rabbitMQChatPubTo.getChatId() + "，原因: " + cause);
                    }

                    //记录发送失败消息
                    mqReturnMessageService.logReturnMessage(rabbitMQMessageTo, message);
                }
                throw new RabbitMQSendException("消息发送失败");
            }
        });

        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            String message = new String(returnedMessage.getMessage().getBody(), StandardCharsets.UTF_8);
            //rabbitmq中ReturnsCallback回退 json 数据在 "" 中，需要去掉 ""，并且需要替换转义字符 \
            message = message.substring(1)
                    .replaceAll("\\\\", "");
            RabbitMQMessageTo<Object> rabbitMQMessageTo = JSONUtil.toBean(message,
                    new TypeReference<RabbitMQMessageTo<Object>>() {},
                    true);

            if(MQMessageType.CHAT_PUB_TYPE.equals(rabbitMQMessageTo.getType())){
                RabbitMQMessageTo<RabbitMQChatPubTo> mqTo = JSONUtil.toBean(message,
                        new TypeReference<RabbitMQMessageTo<RabbitMQChatPubTo>>() {},
                        true);
                //获取聊天发布to
                RabbitMQChatPubTo rabbitMQChatPubTo = mqTo.getMessage();
                log.error("聊天消息发布队列消息被交换机: " + returnedMessage.getExchange() + " 回退，聊天id: "
                        + rabbitMQChatPubTo.getChatId() + "，replyCode: " +
                        returnedMessage.getReplyCode() + "，replyText: " +
                        returnedMessage.getReplyText() + "，routingKey: " +
                        returnedMessage.getRoutingKey());
            }

            //记录发送失败消息
            mqReturnMessageService.logReturnMessage(rabbitMQMessageTo, message);
        });
    }
}
