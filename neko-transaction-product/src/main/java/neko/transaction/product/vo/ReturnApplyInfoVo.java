package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import neko.transaction.product.entity.ReturnApplyImage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 退货申请信息vo
 */
@Data
@Accessors(chain = true)
public class ReturnApplyInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 退款申请id
     */
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
     * 商品id
     */
    private String productId;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品展示图片
     */
    private String displayImage;

    /**
     * 全分类名
     */
    private String fullCategoryName;

    /**
     * 商品价格
     */
    private BigDecimal cost;

    /**
     * 实际支付价格
     */
    private BigDecimal actualCost;

    /**
     * 购买数量
     */
    private Integer number;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 退货申请图片
     */
    private List<ReturnApplyImage> returnApplyImages;
}
