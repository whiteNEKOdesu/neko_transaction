package neko.transaction.product.mapper;

import neko.transaction.product.entity.ProductComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

}
