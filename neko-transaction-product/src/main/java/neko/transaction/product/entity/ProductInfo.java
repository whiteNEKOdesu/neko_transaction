package neko.transaction.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
@Data
@Accessors(chain = true)
@TableName("product_info")
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String productId;

    /**
     * 商品卖家id
     */
    private String uid;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品展示图片
     */
    private String displayImage;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 是否封禁
     */
    private Boolean isBan;

    /**
     * 0->下架，1->上架，2->删除
     */
    private Byte status;

    /**
     * 商品上架申请id
     */
    private String productApplyId;

    /**
     * 上架时间
     */
    private LocalDateTime upTime;

    /**
     * 销量
     */
    private Integer saleNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
