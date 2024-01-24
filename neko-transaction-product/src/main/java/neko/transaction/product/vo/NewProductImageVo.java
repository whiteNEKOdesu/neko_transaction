package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商品图片添加vo
 */
@Data
@Accessors(chain = true)
public class NewProductImageVo implements Serializable {
    /**
     * 商品id
     */
    @NotBlank
    private String productId;

    /**
     * 商品图片
     */
    @NotNull
    private MultipartFile productImage;
}
