package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 班级信息vo
 */
@Data
@Accessors(chain = true)
public class ClassInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 班级id
     */
    private String classId;

    /**
     * 所属专业id
     */
    private Integer majorId;

    /**
     * 所属专业名
     */
    private String majorName;

    /**
     * 所属二级学院id
     */
    private Integer collegeId;

    /**
     * 所属二级学院名
     */
    private String collegeName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
