package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 管理员添加学生信息vo
 */
@Data
@Accessors(chain = true)
public class NewMemberInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @NotBlank
    private String uid;

    /**
     * 所属班级id
     */
    @NotBlank
    private String classId;

    /**
     * 性别，1->男，0->女
     */
    @NotNull
    private Boolean gender;

    /**
     * 真实姓名
     */
    @NotBlank
    private String realName;

    /**
     * 身份证号
     */
    @NotBlank
    @Pattern(regexp = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$")
    private String idCardNumber;
}
