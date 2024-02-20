package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class NewProductCommentVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @NotBlank
    private String orderId;

    /**
     * 商品id
     */
    @NotBlank
    private String productId;

    /**
     * 评论
     */
    @NotBlank
    @Size(min = 1, max = 500)
    private String comment;

    /**
     * 评分，范围 1 - 5
     */
    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Byte score;

    /**
     * 是否匿名
     */
    @NotNull
    private Boolean isNick;
}
