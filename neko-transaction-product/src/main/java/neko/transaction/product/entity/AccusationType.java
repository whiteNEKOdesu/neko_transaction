package neko.transaction.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 举报类型信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-04-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("accusation_type")
public class AccusationType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 举报类型id
     */
    @TableId(value = "accuse_type_id", type = IdType.AUTO)
    private Integer accuseTypeId;

    /**
     * 举报类型
     */
    private String accuseType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
