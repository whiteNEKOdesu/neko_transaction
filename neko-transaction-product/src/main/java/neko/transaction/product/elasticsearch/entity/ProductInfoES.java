package neko.transaction.product.elasticsearch.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * elasticsearch商品信息实体类
 */
@Data
@Accessors(chain = true)
public class ProductInfoES implements Serializable {
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
     * 商品卖家用户名
     */
    private String userName;

    /**
     * 商品卖家真实姓名
     */
    private String realName;

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
