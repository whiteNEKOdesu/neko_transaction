package neko.transaction.commonbase.utils.entity;

/**
 * 举报信息状态常量
 */
public class AccusationStatus {
    /**
     * 未处理
     */
    public static final Byte UNHANDLED = 0;

    /**
     * 封禁
     */
    public static final Byte BANNED = 1;

    /**
     * 未封禁
     */
    public static final Byte UNBANNED = 2;
}
