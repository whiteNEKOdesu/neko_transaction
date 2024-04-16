package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 提交审核退货申请vo
 */
@Data
@Accessors(chain = true)
public class CensorReturnApplyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 退款申请id
     */
    @NotNull
    private Long applyId;

    /**
     * 回应
     */
    @NotBlank
    private String response;

    /**
     * 是否通过
     */
    @NotNull
    private Boolean isPass;
}
