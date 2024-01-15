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
 * 二级学院信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Data
@Accessors(chain = true)
@TableName("college_info")
public class CollegeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 二级学院id
     */
    @TableId(value = "college_id", type = IdType.AUTO)
    private Integer collegeId;

    /**
     * 二级学院名
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
