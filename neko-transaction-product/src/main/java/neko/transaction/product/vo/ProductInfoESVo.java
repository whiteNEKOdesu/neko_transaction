package neko.transaction.product.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import neko.transaction.product.elasticsearch.entity.ProductInfoES;

import java.io.Serializable;
import java.util.List;

/**
 * 商品信息 elasticsearch 查询结果vo
 */
@Data
@Accessors(chain = true)
public class ProductInfoESVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 查询结果
     */
    private List<ProductInfoES> records;

    private Integer total;

    private Integer size;

    private Integer current;
}
