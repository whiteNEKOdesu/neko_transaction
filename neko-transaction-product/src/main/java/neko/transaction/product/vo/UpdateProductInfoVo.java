package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 修改商品信息的vo
 */
@Data
@Accessors(chain = true)
public class UpdateProductInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 要修改的商品id
     */
    @NotBlank
    private String productId;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 商品名
     */
    @Size(min = 1, max = 20)
    private String productName;

    /**
     * 商品描述
     */
    @Size(min = 1, max = 500)
    private String description;

    /**
     * 商品展示图片
     */
    private MultipartFile displayImage;

    /**
     * 商品价格
     */
    @Min(value = 0)
    private BigDecimal price;
}
