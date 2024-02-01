package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 提交订单vo
 */
@Data
@Accessors(chain = true)
public class NewOrderInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 提交订单接口幂等性保证的token
     */
    @NotBlank
    private String token;

    /**
     * 要购买的商品信息
     */
    @Valid
    @NotEmpty
    private List<NewOrderProductInfo> newOrderProductInfos;

    /**
     * 是否从购物车中提交
     */
    @NotNull
    private Boolean isFromPurchaseList;

    /**
     * 商品信息
     */
    @Data
    @Accessors(chain = true)
    public static class NewOrderProductInfo{
        /**
         * 商品id
         */
        @NotBlank
        private String productId;

        /**
         * 购买数量
         */
        @NotNull
        private Integer number;
    }
}
