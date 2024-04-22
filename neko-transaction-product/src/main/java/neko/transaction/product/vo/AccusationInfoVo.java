package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AccusationInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 举报id
     */
    private Long accuseId;

    /**
     * 举报类型id
     */
    private Integer accuseTypeId;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 举报描述
     */
    private String description;

    /**
     * 举报人用户id
     */
    private String informerUid;

    /**
     * 操作的管理员id
     */
    private String operationAdminId;

    /**
     * 处理状态，0->未处理，1->封禁，2->未封禁
     */
    private Byte status;

    /**
     * 举报类型
     */
    private String accuseType;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
