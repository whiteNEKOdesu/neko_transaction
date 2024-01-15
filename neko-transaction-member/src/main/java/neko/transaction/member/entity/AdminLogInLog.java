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
 * 管理员登录记录表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("admin_log_in_log")
public class AdminLogInLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录日志id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String logInId;

    /**
     * 管理员id，对应admin_id表admin_id
     */
    private String adminId;

    /**
     * 登录ip
     */
    private String ip;

    /**
     * 是否登录成功
     */
    private Byte isLogIn;

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
