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
 * 库存锁定日志表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Data
@Accessors(chain = true)
@TableName("stock_lock_log")
public class StockLockLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库存锁定记录id
     */
    @TableId(type = IdType.AUTO)
    private Long stockLockLogId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 库存id
     */
    private String wareId;

    /**
     * 库存锁定数量
     */
    private Integer lockNumber;

    /**
     * -1->已取消锁定，0->锁定中，1->用户已支付
     */
    private Byte status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
