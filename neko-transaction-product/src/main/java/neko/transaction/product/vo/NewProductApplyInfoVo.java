package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 添加上架商品申请vo
 */
@Data
@Accessors(chain = true)
public class NewProductApplyInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品所属的分类id
     */
    @NotNull
    private Integer categoryId;

    /**
     * 商品名
     */
    @NotBlank
    @Size(min = 1, max = 20)
    private String productName;

    /**
     * 商品描述
     */
    @NotBlank
    @Size(min = 1, max = 500)
    private String description;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 申请商品图片
     */
    @NotNull
    private MultipartFile applyImage;
}
