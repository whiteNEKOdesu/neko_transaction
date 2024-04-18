package neko.transaction.member.listener;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.RabbitMqConstant;
import neko.transaction.member.config.ActiveValue;
import neko.transaction.member.config.WebSocket;
import neko.transaction.member.service.MemberChatInfoReadLogService;
import neko.transaction.member.service.MemberChatInfoService;
import neko.transaction.member.to.RabbitMQChatPubTo;
import neko.transaction.member.to.RabbitMQMessageTo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * rabbitmq聊天消息发布队列监听
 */
@Component
@RabbitListener(queues = RabbitMqConstant.CHAT_PUB_QUEUE_NAME)
@Slf4j
public class ChatPubListener {
    @Resource
    private MemberChatInfoReadLogService memberChatInfoReadLogService;

    @Resource
    private MemberChatInfoService memberChatInfoService;

    @Resource
    private WebSocket webSocket;

    /**
     * 监听 rabbitmq 聊天消息发布队列
     */
    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void chatPub(String jsonMessage, Message message, Channel channel) throws IOException {
        RabbitMQMessageTo<RabbitMQChatPubTo> rabbitMQMessageTo = JSONUtil.toBean(jsonMessage,
                new TypeReference<RabbitMQMessageTo<RabbitMQChatPubTo>>() {},
                true);
        //获取聊天发布to
        RabbitMQChatPubTo rabbitMQChatPubTo = rabbitMQMessageTo.getMessage();

        try {
            String fromId = rabbitMQChatPubTo.getFromId(), toId = rabbitMQChatPubTo.getToId();

            //WebSocket 向在线用户发送聊天消息
            if(webSocket.sendOneMessage(toId, rabbitMQChatPubTo.getMessage())){
                //获取未读消息id
                List<Long> chatIds = memberChatInfoService.getUnreadChatIdByFromIdToId(fromId, toId);
                //将消息标记为已读
                memberChatInfoReadLogService.newMemberChatInfoReadLog(chatIds, fromId, toId);
            }

            //单个确认消费消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            e.printStackTrace();
            log.error("聊天消息发布异常，聊天id: " + rabbitMQChatPubTo.getChatId());
            //拒收消息，并让消息重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);

            return;
        }

        if(ActiveValue.isDebug){
            log.info("聊天消息发布，聊天id: " + rabbitMQChatPubTo.getChatId());
        }
    }
}
