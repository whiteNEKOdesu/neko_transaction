package neko.transaction.product.entity;

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
 * 举报信息表
 * </p>
 *
 * @author NEKO
 * @since 2024-04-22
 */
@Data
@Accessors(chain = true)
@TableName("accusation_info")
public class AccusationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 举报id
     */
    @TableId(value = "accuse_id", type = IdType.AUTO)
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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
