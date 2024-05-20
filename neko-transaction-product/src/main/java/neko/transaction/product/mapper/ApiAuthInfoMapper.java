package neko.transaction.product.mapper;

import neko.transaction.product.entity.ApiAuthInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * api鉴权信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-05-20
 */
@Mapper
public interface ApiAuthInfoMapper extends BaseMapper<ApiAuthInfo> {

}
