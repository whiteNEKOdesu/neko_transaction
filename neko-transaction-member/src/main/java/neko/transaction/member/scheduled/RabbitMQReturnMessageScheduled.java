package neko.transaction.member.scheduled;

import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.MQMessageType;
import neko.transaction.commonbase.utils.entity.RabbitMqConstant;
import neko.transaction.member.entity.MqReturnMessage;
import neko.transaction.member.service.MqReturnMessageService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * rabbitmq回退消息重新发送定时任务
 */
@Component
@EnableScheduling
@EnableAsync
@Slf4j
public class RabbitMQReturnMessageScheduled {
    @Resource
    private MqReturnMessageService mqReturnMessageService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0 0 */1 * * ?")
    @Async
    public void resendMessage(){
        log.info("rabbitmq回退消息重新发送定时任务启动");

        List<MqReturnMessage> list = mqReturnMessageService.lambdaQuery()
                .eq(MqReturnMessage::getIsDelete, false)
                .list();

        List<Long> mqReturnIds = list.stream().map(mqReturnMessage -> {
            if(MQMessageType.CHAT_PUB_TYPE.equals(mqReturnMessage.getType())){
                //在CorrelationData中设置回退消息
                CorrelationData correlationData = new CorrelationData(MQMessageType.CHAT_PUB_TYPE.toString());
                String notAvailable = "not available";
                correlationData.setReturned(new ReturnedMessage(new Message(mqReturnMessage.getMessage().getBytes(StandardCharsets.UTF_8)),
                        0,
                        notAvailable,
                        notAvailable,
                        notAvailable));
                //向聊天消息发布队列发送聊天消息，用于聊天消息同步发送
                rabbitTemplate.convertAndSend(RabbitMqConstant.CHAT_PUB_EXCHANGE_NAME,
                        "",
                        mqReturnMessage.getMessage(),
                        correlationData);
            }

            return mqReturnMessage.getMqReturnId();
        }).collect(Collectors.toList());

        if(!mqReturnIds.isEmpty()){
            //删除订单rabbitmq消息发送失败记录
            mqReturnMessageService.deleteMqReturnMessageByMqReturnIds(mqReturnIds);
        }

        log.info("rabbitmq回退消息重新发送定时任务完成，发送消息 " + list.size() + " 条");
    }
}
