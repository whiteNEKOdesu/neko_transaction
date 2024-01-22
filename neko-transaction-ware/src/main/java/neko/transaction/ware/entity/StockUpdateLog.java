package neko.transaction.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存更新日志表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Data
@Accessors(chain = true)
@TableName("stock_update_log")
public class StockUpdateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库存更新记录id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String stockUpdateLogId;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 更新数量偏移量
     */
    private Integer updateNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
