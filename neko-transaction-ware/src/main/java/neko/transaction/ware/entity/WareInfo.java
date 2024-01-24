package neko.transaction.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Data
@Accessors(chain = true)
@TableName("ware_info")
public class WareInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库存id
     */
    @TableId(type = IdType.AUTO)
    private Long wareId;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 锁定库存数量
     */
    private Integer lockNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
