package neko.transaction.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.product.entity.ProductComment;
import neko.transaction.product.vo.ProductScoreVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品评论表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-02-20
 */
@Mapper
public interface ProductCommentMapper extends BaseMapper<ProductComment> {
    /**
     * 根据商品id获取商品评分信息
     * @param productId 商品id
     * @return 商品评分信息
     */
    ProductScoreVo getProductScoreByProductId(String productId);
}
