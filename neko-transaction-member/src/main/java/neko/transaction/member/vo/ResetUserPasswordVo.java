package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 重置密码vo
 */
@Data
@Accessors(chain = true)
public class ResetUserPasswordVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @NotBlank
    private String uid;

    /**
     * 新密码
     */
    @NotBlank
    private String todoPassword;

    /**
     * 验证码
     */
    @NotBlank
    private String code;
}
