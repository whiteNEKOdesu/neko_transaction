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
 * 学生班级信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("class_info")
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班级id
     */
    @TableId(type = IdType.INPUT)
    private String classId;

    /**
     * 所属专业id
     */
    private Integer majorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
