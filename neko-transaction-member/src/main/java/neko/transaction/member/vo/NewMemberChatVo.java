package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 添加聊天消息vo
 */
@Data
@Accessors(chain = true)
public class NewMemberChatVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 接收人id
     */
    @NotBlank
    private String toId;

    /**
     * 消息体信息
     */
    @NotBlank
    private String body;
}
