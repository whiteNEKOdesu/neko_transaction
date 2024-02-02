package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单分页查询vo
 */
@Data
@Accessors(chain = true)
public class OrderInfoPageQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 支付宝流水id
     */
    private String alipayTradeId;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 订单总价
     */
    private BigDecimal cost;

    /**
     * 实际支付价格
     */
    private BigDecimal actualCost;

    /**
     * 订单状态，-1->取消，0->未支付，1->已支付
     */
    private Byte status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 订单涉及的详情商品信息vo集合
     */
    private List<OrderDetailInfoVo> orderDetailInfos;

    /**
     * 订单涉及的详情商品信息vo
     */
    @Data
    @Accessors(chain = true)
    public static class OrderDetailInfoVo{
        /**
         * 订单详情id
         */
        private String orderDetailId;

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
    }
}
