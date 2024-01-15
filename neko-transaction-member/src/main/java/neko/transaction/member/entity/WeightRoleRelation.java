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
 * 权限，角色关系表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("weight_role_relation")
public class WeightRoleRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关系id
     */
    @TableId(value = "relation_id", type = IdType.AUTO)
    private Integer relationId;

    /**
     * 权限id，对应user_weight表weight_id
     */
    private Integer weightId;

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
