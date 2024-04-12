package neko.transaction.commonbase.utils.entity;

/**
 * 退货申请状态常量
 */
public class ReturnApplyStatus {
    /**
     * 店家审核中
     */
    public static final Byte SELLER_CENSORING = 0;

    /**
     * 管理员审核中
     */
    public static final Byte ADMIN_CENSORING = 1;

    /**
     * 货物退还中
     */
    public static final Byte CARGO_RETURNING = 2;

    /**
     * 退款成功
     */
    public static final Byte RETURN_COMPLETED = 3;

    /**
     * 驳回
     */
    public static final Byte REJECTED = 4;
}
