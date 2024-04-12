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
 * 退货申请表
 * </p>
 *
 * @author NEKO
 * @since 2024-04-11
 */
@Data
@Accessors(chain = true)
@TableName("return_apply_info")
public class ReturnApplyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退款申请id
     */
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Long applyId;

    /**
     * 订单详情id
     */
    private String orderDetailId;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 卖家回应
     */
    private String sellerResponse;

    /**
     * 管理员回应
     */
    private String adminResponse;

    /**
     * 操作的管理员id
     */
    private String operateAdminId;

    /**
     * 退款审核状态，0->店家审核中，1->管理员审核中，2->货物退还中，3->退款成功，4->驳回
     */
    private Byte status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
