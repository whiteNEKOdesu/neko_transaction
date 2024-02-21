package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 指定年的订单流水信息vo
 */
@Data
@Accessors(chain = true)
public class OrderTransactionInYearVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 月份
     */
    private Integer transactionMonth;

    /**
     * 订单交易数量
     */
    private Long orderNumber;

    /**
     * 流水数量
     */
    private BigDecimal transactionNumber;
}
