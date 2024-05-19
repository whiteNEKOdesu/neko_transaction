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
 * api鉴权信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-05-19
 */
@Data
@Accessors(chain = true)
@TableName("api_auth_info")
public class ApiAuthInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 后端api id
     */
    @TableId(value = "api_id", type = IdType.AUTO)
    private Long apiId;

    /**
     * api路径
     */
    private String path;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求处理方法
     */
    private String handlerMethod;

    /**
     * 访问要求的权限id
     */
    private Integer weightId;

    /**
     * 权限名
     */
    private String weight;

    /**
     * 访问要求的角色id
     */
    private Integer roleId;

    /**
     * 角色名
     */
    private String role;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
