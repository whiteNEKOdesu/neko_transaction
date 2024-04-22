package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 修改举报类型信息vo
 */
@Data
@Accessors(chain = true)
public class UpdateAccusationTypeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 举报类型id
     */
    @NotNull
    private Integer accuseTypeId;

    /**
     * 举报类型
     */
    @NotBlank
    @Size(min = 1, max = 20)
    private String accuseType;

    /**
     * 排序优先级，倒序排序
     */
    @NotNull
    private Integer sort;
}
