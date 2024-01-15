package neko.transaction.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 学生专业信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("major_info")
public class MajorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专业id
     */
    @TableId(value = "major_id", type = IdType.AUTO)
    private Integer majorId;

    /**
     * 所属二级学院id
     */
    private Integer collegeId;

    /**
     * 专业名
     */
    private String majorName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
