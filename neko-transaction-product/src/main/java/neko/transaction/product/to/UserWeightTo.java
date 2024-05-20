package neko.transaction.product.to;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限信息to
 */
@Data
@Accessors(chain = true)
public class UserWeightTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 权限id
     */
    @TableId(value = "weight_id", type = IdType.AUTO)
    private Integer weightId;

    /**
     * 权限名
     */
    private String weightType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
