package neko.transaction.commonbase.utils.entity;

/**
 * rabbitmq常量
 */
public class RabbitMqConstant {
    /**
     * ============================ 订单处理延迟队列常量配置 ============================
     */
    //订单交换机名
    public static final String ORDER_EXCHANGE_NAME = "neko-transaction-order-exchange";

    //订单处理队列名
    public static final String ORDER_HANDLE_QUEUE_NAME = "neko.transaction.order.handle.queue";

    //订单处理延迟队列名
    public static final String ORDER_HANDLE_DELAY_QUEUE_NAME = "neko.transaction.order.handle.delay.queue";

    //订单处理队列routingKey名
    public static final String ORDER_HANDLE_QUEUE_ROUTING_KEY_NAME = "neko.transaction.order.handle.#";

    //订单处理死信队列routingKey名
    public static final String ORDER_DEAD_LETTER_ROUTING_KEY_NAME = "neko.transaction.order.delay";

    /**
     * ============================ 聊天消息发布队列常量配置 ============================
     */
    //聊天消息发布交换机名
    public static final String CHAT_PUB_EXCHANGE_NAME = "neko-transaction-member-chat-exchange";

    //聊天消息发布队列名
    public static final String CHAT_PUB_QUEUE_NAME = "neko.transaction.member.chat.queue";
}
