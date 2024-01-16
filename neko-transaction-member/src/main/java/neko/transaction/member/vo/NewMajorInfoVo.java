package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加专业信息vo
 */
@Data
@Accessors(chain = true)
public class NewMajorInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 所属二级学院id
     */
    @NotNull
    private Integer collegeId;

    /**
     * 专业名
     */
    @NotBlank
    private String majorName;
}
