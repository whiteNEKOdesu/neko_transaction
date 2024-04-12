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
 * 退货申请图片表
 * </p>
 *
 * @author NEKO
 * @since 2024-04-11
 */
@Data
@Accessors(chain = true)
@TableName("return_apply_image")
public class ReturnApplyImage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退货申请提交图片id
     */
    @TableId(value = "return_image_id", type = IdType.AUTO)
    private Long returnImageId;

    /**
     * 退款申请id
     */
    private Long applyId;

    /**
     * 申请图片
     */
    private String applyImage;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
