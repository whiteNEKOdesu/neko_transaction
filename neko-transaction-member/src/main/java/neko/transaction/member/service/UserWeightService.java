package neko.transaction.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.UserWeight;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface UserWeightService extends IService<UserWeight> {
    /**
     * 添加权限
     * @param weightType 权限名
     */
    void newUserWeight(String weightType);

    /**
     * 分页查询权限信息
     * @param vo 分页查询vo
     * @return 分页查询结果
     */
    Page<UserWeight> getUserWeightByQueryLimitedPage(QueryVo vo);

    /**
     * 查询指定 角色id 未绑定的权限
     * @param roleId 角色id
     * @return 角色id 未绑定的权限
     */
    List<UserWeight> getUnbindWeightByRoleId(Integer roleId);

    /**
     * 根据 权限id 删除权限
     * @param weightId 权限id
     */
    void deleteUserWeight(Integer weightId);

    /**
     * 获取全部权限信息
     * @return 全部权限信息
     */
    List<UserWeight> getAllUserWeight();
}
