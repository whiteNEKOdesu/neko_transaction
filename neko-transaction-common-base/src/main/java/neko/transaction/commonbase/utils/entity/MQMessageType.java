package neko.transaction.commonbase.utils.entity;

public class MQMessageType {
    /**
     * 订单处理延迟队列消息
     */
    public static final Byte ORDER_STATUS_CHECK_TYPE = 0;

    /**
     * 聊天消息发布队列消息
     */
    public static final Byte CHAT_PUB_TYPE = 1;
}
