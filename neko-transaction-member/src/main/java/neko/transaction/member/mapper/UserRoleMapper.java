package neko.transaction.member.mapper;

import neko.transaction.member.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    int getMemberLevelRoleNumberByRoleIds(List<Integer> roleIds);
}
