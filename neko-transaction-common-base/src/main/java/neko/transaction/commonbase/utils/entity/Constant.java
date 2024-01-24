package neko.transaction.commonbase.utils.entity;

public class Constant {
    public static final String REDIS_PREFIX = "neko_transaction:";

    public static final String MEMBER_REDIS_PREFIX = REDIS_PREFIX + "member:";

    public static final String PRODUCT_REDIS_PREFIX = REDIS_PREFIX + "product:";

    public static final String ELASTIC_SEARCH_INDEX = "neko_transaction_product";

    /**
     * 图片大小限制
     */
    public static final long IMAGE_MAX_SIZE = 1024 * 1024 * 10;

    /**
     * 每个商品的最大上传图片数量
     */
    public static final int MAX_IMAGE_NUM_PER_PRODUCT = 10;
}
