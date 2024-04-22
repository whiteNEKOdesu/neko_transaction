package neko.transaction.product.mapper;

import neko.transaction.product.entity.AccusationType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 举报类型信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-04-22
 */
@Mapper
public interface AccusationTypeMapper extends BaseMapper<AccusationType> {

}
