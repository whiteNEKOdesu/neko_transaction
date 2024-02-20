package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 学号，密码登录vo
 */
@Data
@Accessors(chain = true)
public class UidLogInVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @NotBlank
    private String uid;

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
