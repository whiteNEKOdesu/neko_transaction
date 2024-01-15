package neko.transaction.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 聊天消息信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("membet_chat_info")
public class MembetChatInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 聊天id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String chatId;

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
     * 消息类型，0->用户之间的消息，1->用户，管理员之间的消息
     */
    private Byte type;

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
