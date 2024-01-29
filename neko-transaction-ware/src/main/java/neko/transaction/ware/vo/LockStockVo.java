package neko.transaction.ware.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 库存锁定vo
 */
@Data
@Accessors(chain = true)
public class LockStockVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @NotBlank
    private String orderId;

    /**
     * 要锁定的商品信息
     */
    @Valid
    @NotEmpty
    private List<LockProductInfo> lockProductInfos;

    /**
     * 要锁定的商品信息
     */
    @Data
    @Accessors(chain = true)
    public static class LockProductInfo{
        /**
         * 商品id
         */
        @NotBlank
        private String productId;

        /**
         * 库存锁定数量
         */
        @NotNull
        private Integer lockNumber;
    }
}
