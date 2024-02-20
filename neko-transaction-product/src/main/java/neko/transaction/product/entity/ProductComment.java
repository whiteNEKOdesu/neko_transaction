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
 * 商品评论表
 * </p>
 *
 * @author NEKO
 * @since 2024-02-20
 */
@Data
@Accessors(chain = true)
@TableName("product_comment")
public class ProductComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 评论者学号
     */
    private String uid;

    /**
     * 评分，范围 1 - 5
     */
    private Byte score;

    /**
     * 评论者姓名
     */
    private String realName;

    /**
     * 是否匿名
     */
    private Byte isNick;

    /**
     * 是否删除
     */
    private Byte isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
