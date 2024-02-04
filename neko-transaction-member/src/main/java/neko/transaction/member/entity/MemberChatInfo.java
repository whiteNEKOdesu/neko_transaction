package neko.transaction.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 聊天消息信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("member_chat_info")
public class MemberChatInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 聊天id
     */
    @TableId(type = IdType.AUTO)
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
     * 消息体信息
     */
    private String body;

    /**
     * 是否删除
     */
    private Byte isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
