package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 订单详情按照状态聚合vo
 */
@Data
@Accessors(chain = true)
public class OrderDetailStatusAggVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单详情状态
     */
    private String status;

    /**
     * 数量
     */
    private Long number;
}
