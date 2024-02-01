package neko.transaction.product.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 添加订单的 redis to，用于记录购买的商品信息
 */
@Data
@Accessors(chain = true)
public class NewOrderRedisTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 订单中所有的商品信息
     */
    private List<ProductsInOrder> productsInOrders;

    /**
     * 是否从购物车中提交
     */
    private Boolean isFromPurchaseList;

    /**
     * 订单的商品信息
     */
    @Data
    @Accessors(chain = true)
    public static class ProductsInOrder{
        /**
         * 商品id
         */
        private String productId;

        /**
         * 商品价格
         */
        private BigDecimal cost;

        /**
         * 实际支付价格
         */
        private BigDecimal actualCost;

        /**
         * 购买数量
         */
        private Integer number;
    }
}
