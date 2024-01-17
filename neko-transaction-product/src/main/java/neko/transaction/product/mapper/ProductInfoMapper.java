package neko.transaction.product.mapper;

import neko.transaction.product.entity.ProductInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@Mapper
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {

}
