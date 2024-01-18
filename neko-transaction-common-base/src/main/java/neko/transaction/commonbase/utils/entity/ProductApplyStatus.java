package neko.transaction.commonbase.utils.entity;

/**
 * 商品上架申请状态常量
 */
public class ProductApplyStatus {
    /**
     * 待审核
     */
    public static final Byte UNHANDLED = 0;

    /**
     * 通过
     */
    public static final Byte PASSED = 1;

    /**
     * 未通过
     */
    public static final Byte REJECTED = 2;
}
