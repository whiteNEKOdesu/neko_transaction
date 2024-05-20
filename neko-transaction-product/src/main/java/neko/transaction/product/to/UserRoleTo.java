package neko.transaction.product.to;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色信息to
 */
@Data
@Accessors(chain = true)
public class UserRoleTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    /**
     * 角色名
     */
    private String roleType;

    /**
     * 角色类型种类，0->普通角色类型，1->管理员角色类型，2->会员等级角色类型
     */
    private Byte type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
