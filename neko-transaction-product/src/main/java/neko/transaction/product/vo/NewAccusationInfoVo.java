package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 添加举报信息vo
 */
@Data
@Accessors(chain = true)
public class NewAccusationInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 举报类型id
     */
    @NotNull
    private Integer accuseTypeId;

    /**
     * 商品id
     */
    @NotBlank
    private String productId;

    /**
     * 举报描述
     */
    @NotBlank
    @Size(min = 1, max = 500)
    private String description;
}
