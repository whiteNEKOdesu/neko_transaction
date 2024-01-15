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
 * 用户，角色关系表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("user_role_relation")
public class UserRoleRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关系id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String relationId;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 角色id，对应user_role表role_id
     */
    private Integer roleId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
