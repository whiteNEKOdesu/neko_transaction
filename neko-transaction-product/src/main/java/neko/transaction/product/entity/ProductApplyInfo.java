package neko.transaction.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品上架申请信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("product_apply_info")
public class ProductApplyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品上架申请id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String productApplyId;

    /**
     * 申请用户id
     */
    private String uid;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 申请商品图片
     */
    private String applyImage;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 0->待审核，1->通过，2->未通过
     */
    private Byte status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
