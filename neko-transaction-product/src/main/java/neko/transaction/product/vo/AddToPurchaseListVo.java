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
 * 添加商品到购物车的vo
 */
@Data
@Accessors(chain = true)
public class AddToPurchaseListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 要添加的商品信息
     */
    @Valid
    @NotEmpty
    private List<PurchaseListProductInfo> productInfos;

    /**
     * 商品信息
     */
    @Data
    @Accessors(chain = true)
    public static class PurchaseListProductInfo{
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
