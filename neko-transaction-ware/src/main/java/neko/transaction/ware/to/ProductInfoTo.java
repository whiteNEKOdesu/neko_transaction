package neko.transaction.ware.to;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品信息to
 */
@Data
@Accessors(chain = true)
public class ProductInfoTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
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
     * 全分类名
     */
    private String fullCategoryName;

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
     * 上架时间
     */
    private LocalDateTime upTime;

    /**
     * 商品上架申请id
     */
    private String productApplyId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
