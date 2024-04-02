package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 添加权限，角色关系vo
 */
@Data
@Accessors(chain = true)
public class NewWeightRoleRelationVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 绑定的角色id
     */
    @NotNull
    private Integer roleId;

    /**
     * 添加的权限id List
     */
    @NotEmpty
    private List<Integer> weightIds;
}
