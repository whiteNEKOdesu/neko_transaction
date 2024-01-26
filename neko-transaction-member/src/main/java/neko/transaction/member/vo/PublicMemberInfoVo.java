package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户公开信息vo
 */
@Data
@Accessors(chain = true)
public class PublicMemberInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    private String uid;

    /**
     * 所属班级id
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
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userImagePath;

    /**
     * 性别
     */
    private Byte gender;

    /**
     * 真实姓名
     */
    private String realName;
}
