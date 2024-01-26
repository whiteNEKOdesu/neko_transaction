package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品详细信息vo
 */
@Data
@Accessors(chain = true)
public class ProductDetailInfoVo implements Serializable {
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
     * 上架时间
     */
    private String upTime;

    /**
     * 销量
     */
    private Integer saleNumber;
}
