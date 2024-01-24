package neko.transaction.ware.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存信息vo
 */
@Data
@Accessors(chain = true)
public class WareInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 库存id
     */
    private Long wareId;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 库存数量，当前库存数量 - 锁定库存数量
     */
    private Integer stock;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
