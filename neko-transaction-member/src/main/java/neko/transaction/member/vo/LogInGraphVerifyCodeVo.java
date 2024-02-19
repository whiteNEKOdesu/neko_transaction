package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 登录的 Base64 图形验证码vo
 */
@Data
@Accessors(chain = true)
public class LogInGraphVerifyCodeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 验证码追踪id
     */
    private String traceId;

    /**
     * Base64 图形验证码
     */
    private String base64Graph;
}
