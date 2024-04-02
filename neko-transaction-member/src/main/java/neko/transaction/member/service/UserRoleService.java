package neko.transaction.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.UserRole;
import neko.transaction.member.vo.NewUserRoleVo;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface UserRoleService extends IService<UserRole> {
    /**
     * 添加角色
     * @param vo 添加角色vo
     */
    void newUserRole(NewUserRoleVo vo);

    /**
     * 分页查询角色信息
     * @param vo 分页查询vo
     * @return 分页查询结果
     */
    Page<UserRole> getUserRolesByQueryLimitedPage(QueryVo vo);

    /**
     * 获取管理员角色
     * @return 管理员角色
     */
    List<UserRole> getAdminRoles();

    /**
     * 根据角色名获取角色信息
     * @param roleType 角色名
     * @return 角色信息
     */
    UserRole getUserRoleByRoleType(String roleType);

    /**
     * 根据 角色id 删除角色
     * @param roleId 角色id
     */
    void deleteUserRole(Integer roleId);
}
