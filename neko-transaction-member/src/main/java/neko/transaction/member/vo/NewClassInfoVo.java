package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加班级vo
 */
@Data
@Accessors(chain = true)
public class NewClassInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 班级id
     */
    @NotBlank
    private String classId;

    /**
     * 所属专业id
     */
    @NotNull
    private Integer majorId;
}
