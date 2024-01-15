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
 * 管理员表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("admin_info")
public class AdminInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String adminId;

    /**
     * 管理员名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 用户头像
     */
    private String userImagePath;

    /**
     * 指认管理员id
     */
    private String createAdminId;

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
