package neko.transaction.ware.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单信息to
 */
@Data
@Accessors(chain = true)
public class OrderInfoTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 支付宝流水id
     */
    private String alipayTradeId;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 订单总价
     */
    private BigDecimal cost;

    /**
     * 实际支付价格
     */
    private BigDecimal actualCost;

    /**
     * 订单状态，-1->取消，0->未支付，1->已支付
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
