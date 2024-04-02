package neko.transaction.member.mapper;

import neko.transaction.member.entity.UserWeight;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Mapper
public interface UserWeightMapper extends BaseMapper<UserWeight> {
    /**
     * 根据权限名获取权限信息
     * @param weightType 权限名
     * @return 权限信息
     */
    UserWeight getUserWeightByWeightType(String weightType);

    /**
     * 查询指定 角色id 未绑定的权限
     * @param roleId 角色id
     * @return 角色id 未绑定的权限
     */
    List<UserWeight> getUnbindUserWeightByRoleId(Integer roleId);
}
