package neko.transaction.product.mapper;

import neko.transaction.product.entity.ApiAuthInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 修改 api 鉴权信息
     * @param apiId 后端api id
     * @param roleId 访问要求的角色id
     * @param role 角色名
     * @param weightId 访问要求的权限id
     * @param weight 权限名
     */
    void updateApiAuthInfo(Long apiId,
                           Integer roleId,
                           String role,
                           Integer weightId,
                           String weight);
}
