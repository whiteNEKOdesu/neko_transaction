package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录vo
 */
@Data
@Accessors(chain = true)
public class LogInVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @NotBlank
    private String userName;

    /**
     * 密码
     */
    @NotBlank
    private String userPassword;

    /**
     * 验证码追踪id
     */
    @NotBlank
    private String traceId;

    /**
     * 验证码
     */
    @NotBlank
    private String code;
}
