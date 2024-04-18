package neko.transaction.member.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * rabbitmq 聊天消息发布to
 */
@Data
@Accessors(chain = true)
public class RabbitMQChatPubTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 聊天id
     */
    private Long chatId;

    /**
     * 聊天发起人id
     */
    private String fromId;

    /**
     * 接收人id
     */
    private String toId;

    /**
     * 消息
     */
    private String message;
}
