package neko.transaction.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 学生信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("member_info")
public class MemberInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String uid;

    /**
     * 所属班级id
     */
    private Integer classId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 密码盐
     */
    private String salt;

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
     * 是否封禁
     */
    private Byte isBan;

    /**
     * 是否删除
     */
    private Byte isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
