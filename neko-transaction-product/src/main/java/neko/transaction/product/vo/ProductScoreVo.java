package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品评分信息vo
 */
@Data
@Accessors(chain = true)
public class ProductScoreVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分数
     */
    private BigDecimal score;

    /**
     * 评分人数
     */
    private Long number;
}
