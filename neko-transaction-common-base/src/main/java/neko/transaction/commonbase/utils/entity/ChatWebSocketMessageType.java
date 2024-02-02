package neko.transaction.commonbase.utils.entity;

/**
 * 聊天 WebSocket 消息类型常量
 */
public class ChatWebSocketMessageType {
    /**
     * 初始化消息
     */
    public static final Byte INIT = 0;

    /**
     * 聊天消息
     */
    public static final Byte CHAT = 1;

    /**
     * 心跳检测消息
     */
    public static final Byte HEALTH_CHAT = 2;
}
