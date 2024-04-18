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
 * rabbitmq消息发送失败记录表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("mq_return_message")
public class MqReturnMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * rabbitmq回退消息id
     */
    @TableId(value = "mq_return_id", type = IdType.AUTO)
    private Long mqReturnId;

    /**
     * 消息
     */
    private String message;

    /**
     * 消息类型，1->聊天消息发布队列消息
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
     * 修改时间
     */
    private LocalDateTime updateTime;
}
