package neko.transaction.product.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 锁定库存to
 */
@Data
@Accessors(chain = true)
public class LockStockTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 要锁定的商品信息
     */
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
        private String productId;

        /**
         * 库存锁定数量
         */
        private Integer lockNumber;
    }
}
