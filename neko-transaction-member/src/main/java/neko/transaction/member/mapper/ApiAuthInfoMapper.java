package neko.transaction.member.mapper;

import neko.transaction.member.entity.ApiAuthInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    /**
     * 批量修改 api 鉴权信息
     * @param apiAuthInfos api 鉴权信息 list
     */
    int updateBatch(List<ApiAuthInfo> apiAuthInfos);

    /**
     * 批量添加 api 鉴权信息
     * @param apiAuthInfos api 鉴权信息 list
     */
    void insertBatch(List<ApiAuthInfo> apiAuthInfos);
}
