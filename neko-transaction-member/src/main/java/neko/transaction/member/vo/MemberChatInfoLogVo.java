package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户聊天消息对象vo
 */
@Data
@Accessors(chain = true)
public class MemberChatInfoLogVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 聊天发起人id
     */
    private String fromId;

    /**
     * 发送人真实姓名
     */
    private String realName;

    /**
     * 未读消息数量
     */
    private Integer unreadNumber;
}
