package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 聊天 WebSocket vo
 */
@Data
@Accessors(chain = true)
public class ChatWebSocketVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     */
    private Byte type;

    /**
     * 消息
     */
    private String message;

    /**
     * 鉴权 token
     */
    private String token;
}
