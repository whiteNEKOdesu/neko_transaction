package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改 api 鉴权信息vo
 */
@Data
@Accessors(chain = true)
public class UpdateApiAuthInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 后端api id
     */
    @NotNull
    private Long apiId;

    /**
     * 访问要求的权限id
     */
    private Integer weightId;

    /**
     * 访问要求的角色id
     */
    private Integer roleId;
}
