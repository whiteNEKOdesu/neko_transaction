package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 添加退货申请vo
 */
@Data
@Accessors(chain = true)
public class NewReturnApplyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单详情id
     */
    @NotBlank
    @Size(min = 1, max = 20)
    private String orderDetailId;

    /**
     * 退款原因
     */
    @NotBlank
    @Size(min = 1, max = 500)
    private String reason;

    /**
     * 申请图片
     */
    @NotEmpty
    @Size(min = 1, max = 10)
    private List<MultipartFile> files;
}
