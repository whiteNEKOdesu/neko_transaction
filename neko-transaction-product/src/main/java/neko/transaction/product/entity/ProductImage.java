package neko.transaction.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品图片信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@Data
@Accessors(chain = true)
@TableName("product_image")
public class ProductImage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品图片id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String productImageId;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 商品图片
     */
    private String productImage;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
