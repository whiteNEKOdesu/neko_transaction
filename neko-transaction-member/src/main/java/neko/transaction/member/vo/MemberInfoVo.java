package neko.transaction.member.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class MemberInfoVo implements Serializable {
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
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userImagePath;

    /**
     * 权限类型
     */
    private List<String> weightTypes;

    /**
     * 角色类型
     */
    private List<String> roleTypes;

    /**
     * 登录下发的 token
     */
    private String token;

    /**
     * 性别
     */
    private Byte gender;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCardNumber;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
