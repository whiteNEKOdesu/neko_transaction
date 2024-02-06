package neko.transaction.commonbase.utils.entity;

/**
 * 订单详情常量
 */
public class OrderDetailInfoStatus {
    /**
     * 待收货
     */
    public static final Byte WAITING_FOR_RECEIVE = 0;

    /**
     * 已确认收货
     */
    public static final Byte RECEIVED = 1;

    /**
     * 申请退货中
     */
    public static final Byte APPLYING_SEND_BACK = 2;

    /**
     * 已退货
     */
    public static final Byte SENT_BACK = 3;
}
