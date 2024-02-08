package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情vo
 */
@Data
@Accessors(chain = true)
public class OrderDetailInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单详情id
     */
    private String orderDetailId;

    /**
     * 订单id
     */
    private String orderId;

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
     * 卖家学号
     */
    private String sellerUid;

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
     * 订单商品状态，0->待收货，1->已确认收货，2->申请退货中，3->已退货
     */
    private Byte status;

    /**
     * 买家学号
     */
    private String uid;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
