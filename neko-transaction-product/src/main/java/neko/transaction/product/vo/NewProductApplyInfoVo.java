package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
     * 申请用户的学号
     */
    @NotBlank
    private String uid;

    /**
     * 商品所属的分类id
     */
    @NotNull
    private Integer categoryId;

    /**
     * 商品描述
     */
    @NotBlank
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
