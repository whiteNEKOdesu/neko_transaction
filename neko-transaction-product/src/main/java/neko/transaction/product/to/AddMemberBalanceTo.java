package neko.transaction.product.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 添加用户余额to
 */
@Data
@Accessors(chain = true)
public class AddMemberBalanceTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    private String uid;

    /**
     * 添加的余额
     */
    private BigDecimal addNumber;
}
