package neko.transaction.member.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * rabbitmq 消息to
 * @param <T> 消息类型泛型
 */
@Data
@Accessors(chain = true)
public class RabbitMQMessageTo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 消息
     */
    private T message;

    /**
     * 消息类型，0->订单处理延迟队列消息
     */
    private Byte type;

    /**
     * 生成 rabbitmq 消息 to
     * @param message 消息
     * @param type 消息类型，0->订单处理延迟队列消息
     * @return rabbitmq 消息 to
     * @param <T> 消息类型泛型
     */
    public static <T> RabbitMQMessageTo<T> generateMessage(T message, Byte type){
        return new RabbitMQMessageTo<T>()
                .setMessage(message)
                .setType(type);
    }
}
