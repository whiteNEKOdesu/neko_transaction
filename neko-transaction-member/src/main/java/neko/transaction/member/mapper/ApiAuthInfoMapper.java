package neko.transaction.member.mapper;

import neko.transaction.member.entity.ApiAuthInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * api鉴权信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-05-19
 */
@Mapper
public interface ApiAuthInfoMapper extends BaseMapper<ApiAuthInfo> {

}
