package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 邮箱登录vo
 */
@Data
@Accessors(chain = true)
public class EmailLogInVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 邮箱
     */
    @NotBlank
    private String mail;

    /**
     * 验证码
     */
    @NotBlank
    private String code;
}
