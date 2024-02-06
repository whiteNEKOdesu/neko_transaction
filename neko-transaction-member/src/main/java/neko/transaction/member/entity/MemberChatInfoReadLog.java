package neko.transaction.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 聊天消息已读记录表
 * </p>
 *
 * @author NEKO
 * @since 2024-02-05
 */
@Data
@Accessors(chain = true)
@TableName("member_chat_info_read_log")
public class MemberChatInfoReadLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 查看id
     */
    @TableId(value = "read_id", type = IdType.AUTO)
    private Long readId;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
